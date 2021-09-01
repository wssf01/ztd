package com.bike.ztd.controller;


import com.bike.ztd.dto.UserImportDTO;
import com.bike.ztd.dto.UserImportTotalDTO;
import com.bike.ztd.exception.BusinessException;
import com.bike.ztd.qo.UserAddQO;
import com.bike.ztd.qo.UserQO;
import com.bike.ztd.service.ImportService;
import com.bike.ztd.service.TUserService;
import com.bike.ztd.util.DateUtils;
import com.bike.ztd.util.RedisUtils;
import com.bike.ztd.util.ResponseEntity;
import com.bike.ztd.util.UUIDUtils;
import com.bike.ztd.vo.UserVO;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.bike.ztd.util.DateUtils.DATE_FORMAT3;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@RestController
@RequestMapping("/tUser")
public class TUserController {

    @Autowired
    private TUserService userService;

    @Autowired
    private ImportService importService;

    @GetMapping("/userInfo")
    @ApiOperation(value = "用户信息", response = UserVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id")
    })
    public ResponseEntity userInfo(@RequestParam(value = "userId", required = false) String userId) {
        return ResponseEntity.success(userService.userInfo(userId));
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id")
    })
    public ResponseEntity delete(@RequestParam(value = "userId") String userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "用户列表", response = UserVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchValue", value = "关键字查询，姓名与手机号模糊查询"),
            @ApiImplicitParam(name = "current", value = "页码"),
            @ApiImplicitParam(name = "size", value = "每页显示个数")
    })
    public ResponseEntity list(@RequestParam(value = "searchValue", required = false) String searchValue,
                               @RequestParam(value = "current", defaultValue = "1") int current,
                               @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.success(userService.list(searchValue, current, size));
    }

    @PutMapping("/modify")
    @ApiOperation(value = "修改用户信息")
    public ResponseEntity modify(@RequestBody UserQO qo) {
        userService.modify(qo);
        return ResponseEntity.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增用户信息", response = String.class)
    public ResponseEntity add(@RequestBody @Valid UserAddQO qo) {
        return ResponseEntity.success(userService.add(qo));
    }

    @GetMapping("/import/result")
    @ApiOperation(value = "导入结果查询", response = UserImportTotalDTO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "import接口返回的结果")
    })
    public ResponseEntity imp(@RequestParam("uuid") String uuid) {
        return ResponseEntity.success(importService.importInfo(uuid));
    }

    @PostMapping("/import")
    @ApiOperation(value = "批量导入用户", response = String.class)
    public ResponseEntity importUser(@RequestParam("file") MultipartFile file) {
        //验证文件类型
        String fileName = file.getOriginalFilename();
        importService.checkFileType(fileName);

        //读取数据
        List<UserImportDTO> list;
        try (InputStream inputStream = file.getInputStream()) {
            list = readExcel(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.failure("format_error", "格式错误，请检查格式问题");
        }
        if (list == null) {
            return ResponseEntity.failure("empty_limit", "导入的条数为空");
        }
        String uuid = UUIDUtils.timeBasedStr();
        int total = list.size();
        UserImportTotalDTO totalResult = UserImportTotalDTO.build(total);
        RedisUtils.set(importService.importTotalKey(uuid), totalResult, importService.importKeyExpire());
        //异步导入
        importService.imports(list, totalResult, uuid);
        return ResponseEntity.success(uuid);
    }

    private List<UserImportDTO> readExcel(InputStream inputStream) {
        try (Workbook book = WorkbookFactory.create(inputStream)) {
            Iterator<Sheet> it = book.sheetIterator();

            List<List<String>> rs = Lists.newArrayList();
            while (it.hasNext()) {
                Sheet sheet = it.next();
                List<List<String>> rsList = getRowValue(sheet);
                rs.addAll(rsList);
            }
            Set<String> phoneSet = new HashSet<>();
            Set<String> set = new HashSet<>();
            if (!rs.isEmpty()) {
                List<UserImportDTO> list = rs.stream().map(l -> UserImportDTO.build(l, phoneSet, set)).collect(Collectors.toList());
                if (!set.isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    set.forEach(v -> builder.append("[").append(v).append("]"));
                    throw new BusinessException("phone_repeat_error", "导入文档中手机号存在重复: " + builder.toString());
                }
                return list;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<List<String>> getRowValue(Sheet sheet) {
        List<List<String>> list = new ArrayList<>();
        int sheetRowNum = sheet.getLastRowNum();
        for (int numRow = 1; numRow <= sheetRowNum; numRow++) {
            Row row = sheet.getRow(numRow);
            if (row == null || row.getPhysicalNumberOfCells() == 0) {
                continue;
            }
            int blankRow = 0;
            List<String> cells = new ArrayList<>();
            for (int numCell = 0; numCell < 2; numCell++) {
                Cell cell = row.getCell(numCell);
                String cellValue = null;
                if (cell != null) {
                    try {
                        cellValue = getValue(cell);
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }
                }
                cells.add(cellValue);
                if (StringUtils.isBlank(cellValue) && ++blankRow > 10) {
                    break;
                }
            }
            if (blankRow < 10) {
                list.add(cells);
            }
        }
        return list;
    }

    /**
     * 读取数据
     *
     * @param cell
     * @return
     */
    private static String getValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return DateUtils.getDate(cell.getDateCellValue(), DATE_FORMAT3);
            } else {
                return NumberToTextConverter.toText(cell.getNumericCellValue());
            }
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }
}

