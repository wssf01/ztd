package com.bike.ztd.controller;


import com.bike.ztd.qo.WaybillAddQO;
import com.bike.ztd.qo.WaybillCompleteQO;
import com.bike.ztd.qo.WaybillInfoAddQO;
import com.bike.ztd.service.ExportService;
import com.bike.ztd.service.TWaybillService;
import com.bike.ztd.util.RedisUtils;
import com.bike.ztd.util.ResponseEntity;
import com.bike.ztd.util.UUIDUtils;
import com.bike.ztd.vo.MyWaybillListVO;
import com.bike.ztd.vo.WaybillDetailsVO;
import com.bike.ztd.vo.WaybillListVO;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@Api(value = "运单")
@RestController
@Controller
@RequestMapping("/tWaybill")
public class TWaybillController {

    @Autowired
    private TWaybillService waybillService;

    @Autowired
    private ExportService exportService;

    private static final String WAYBILL_EXPORT_KEY = "waybill.export.key:";

    @PostMapping("/add")
    @ApiOperation(value = "创建运单", response = String.class)
    public ResponseEntity addWaybill(@RequestBody WaybillAddQO qo) {
        String waybillId = waybillService.addWaybill(qo);
        return ResponseEntity.success(waybillId);
    }

    @PostMapping("/complete")
    @ApiOperation(value = "完成运单")
    public ResponseEntity completeWaybill(@RequestBody WaybillCompleteQO qo) {
        waybillService.completeWaybill(qo);
        return ResponseEntity.success();
    }

    @PutMapping("/delete")
    @ApiOperation(value = "删除运单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "waybillId", value = "运单id")
    })
    public ResponseEntity deleteWaybill(@RequestParam(value = "waybillId") String waybillId) {
        waybillService.deleteWaybill(waybillId);
        return ResponseEntity.success();
    }

    @GetMapping("/myList")
    @ApiOperation(value = "我的运单列表", response = MyWaybillListVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "页码"),
            @ApiImplicitParam(name = "size", value = "页面大小"),
            @ApiImplicitParam(name = "startAt", value = "开始时间"),
            @ApiImplicitParam(name = "endAt", value = "结束时间")
    })
    public ResponseEntity myList(@RequestParam(value = "current", defaultValue = "1") int current,
                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                 @RequestParam(value = "startAt", required = false) Long startAt,
                                 @RequestParam(value = "endAt", required = false) Long endAt) {
        return ResponseEntity.success(waybillService.myList(current, size, startAt, endAt));
    }

    @GetMapping("/list")
    @ApiOperation(value = "运单列表", response = WaybillListVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id"),
            @ApiImplicitParam(name = "userPhone", value = "用户手机"),
            @ApiImplicitParam(name = "local", value = "城市"),
            @ApiImplicitParam(name = "current", value = "页码"),
            @ApiImplicitParam(name = "size", value = "页面大小"),
            @ApiImplicitParam(name = "startAt", value = "开始时间"),
            @ApiImplicitParam(name = "endAt", value = "结束时间")
    })
    public ResponseEntity list(@RequestParam(value = "userId", required = false) String userId,
                               @RequestParam(value = "userPhone", required = false) String userPhone,
                               @RequestParam(value = "waybillLocal", required = false) String waybillLocal,
                               @RequestParam(value = "current", defaultValue = "1") int current,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               @RequestParam(value = "startAt", required = false) Long startAt,
                               @RequestParam(value = "endAt", required = false) Long endAt) {
        return ResponseEntity.success(waybillService.list(userId, userPhone, waybillLocal, current, size, startAt, endAt));
    }

    @GetMapping("/details")
    @ApiOperation(value = "运单详情", response = WaybillDetailsVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "waybillId", value = "运单id")
    })
    public ResponseEntity details(@RequestParam(value = "waybillId") String waybillId) {
        return ResponseEntity.success(waybillService.details(waybillId));
    }


    @GetMapping(value = "/list/export")
    @ApiOperation(value = "导出运单列表", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id"),
            @ApiImplicitParam(name = "userPhone", value = "用户手机"),
            @ApiImplicitParam(name = "local", value = "城市"),
            @ApiImplicitParam(name = "startAt", value = "开始时间"),
            @ApiImplicitParam(name = "endAt", value = "结束时间")
    })
    public ResponseEntity export(@RequestParam(value = "userId", required = false) String userId,
                                 @RequestParam(value = "userPhone", required = false) String userPhone,
                                 @RequestParam(value = "local", required = false) String local,
                                 @RequestParam(value = "startAt", required = false) Long startAt,
                                 @RequestParam(value = "endAt", required = false) Long endAt) {
        String key = WAYBILL_EXPORT_KEY + UUIDUtils.timeBasedStr();
        Map<String, Object> map = Maps.newHashMap();
        map.put("userId", userId);
        map.put("userPhone", userPhone);
        map.put("local", local);
        if (startAt != null) {
            map.put("startAt", new Date(startAt));
        }
        if (endAt != null) {
            map.put("endAt", new Date(endAt));
        }
        exportService.listExport(key, map);
        RedisUtils.set(key, "", 300000);
        return ResponseEntity.success(key);
    }


    @GetMapping("/checkExport")
    @ApiOperation(value = "检查导出结果", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "key,/list/export接口返回结果，查询导出文件oss文件名")
    })
    public ResponseEntity checkExport(@RequestParam(value = "key") String key) {
        Map<String, String> map = Maps.newHashMap();
        map.put("oss", waybillService.checkExport(key));
        return ResponseEntity.success(map);
    }

    @PostMapping("/addInfo")
    @ApiOperation(value = "添加车辆信息")
    public ResponseEntity addInfo(@RequestBody WaybillInfoAddQO qo) {
        waybillService.addInfo(qo);
        return ResponseEntity.success();
    }
}

