package com.bike.ztd.service.impl;

import com.bike.ztd.dto.UserImportDTO;
import com.bike.ztd.dto.UserImportResultDTO;
import com.bike.ztd.dto.UserImportTotalDTO;
import com.bike.ztd.entity.TUser;
import com.bike.ztd.enums.UserStatus;
import com.bike.ztd.exception.BusinessException;
import com.bike.ztd.qo.UserAddQO;
import com.bike.ztd.service.ImportService;
import com.bike.ztd.service.TUserService;
import com.bike.ztd.util.CheckUtils;
import com.bike.ztd.util.RedisUtils;
import com.bike.ztd.util.UUIDUtils;
import com.bike.ztd.util.ValidatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
public class ImportServiceImpl implements ImportService {

    // 用于优先加载
    @Resource
    private RedisTemplate<String, UserImportResultDTO> redisTemplate;

    @Autowired
    private TUserService userService;

    @Override
    public void checkFileType(String name) {
        //验证文件类型
        if (StringUtils.isBlank(name)) {
            throw new BusinessException("import_error", "文件格式不正确");
        }
        String suffix = name.substring(name.lastIndexOf("."));
        if (!".xlsx".equals(suffix.toLowerCase()) && !".xls".equals(suffix.toLowerCase())) {
            throw new BusinessException("import_error", "文件格式不正确");
        }
    }

    @Async
    @Override
    public void imports(List<UserImportDTO> list, UserImportTotalDTO totalResult, String uuid) {
        int total = totalResult.getTotal();
        int restCount = total;
        int success = 0;
        int failure = 0;

        String importFailureKey = importFailureKey(uuid);
        String importTotalKey = importTotalKey(uuid);
        long importKeyExpire = importKeyExpire();


        for (int i = 0; i < total; i++) {
            StringBuilder builder = new StringBuilder();

            UserImportDTO dto = list.get(i);

            Map<String, String> map = ValidatorUtils.validator(dto);
            if (MapUtils.isNotEmpty(map)) {
                map.forEach((k, v) -> builder.append(",").append(v));
            }

            if (StringUtils.isNotBlank(dto.getName()) && dto.getName().length() > 30) {
                builder.append(",").append("姓名长度不能超过30位");
            }

            if (!CheckUtils.isMobilePhone(dto.getPhone())) {
                builder.append(",").append("手机号格式有误");
            }

            if (builder.length() == 0) {
                try {
                    insertUser(dto);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    if (e instanceof BusinessException) {
                        builder.append(",").append(((BusinessException) e).getMsg());
                    } else if (e.getMessage().contains("unique_phone")) {
                        builder.append(",").append("手机号已存在");
                    } else {
                        builder.append(",").append("格式异常");
                    }
                }
            }

            // 导入成功一条
            if (builder.length() == 0) {
                totalResult.setSuccess(++success);
            } else {    // 失败处理
                totalResult.setFailure(++failure);
                redisTemplate.opsForList().leftPush(importFailureKey, UserImportResultDTO.build(dto, builder.toString(), i + 1));
                redisTemplate.expire(importFailureKey, importKeyExpire, TimeUnit.MILLISECONDS);
            }
            totalResult.setRestCount(--restCount);

            RedisUtils.set(importTotalKey, totalResult, importKeyExpire);
        }
        log.info("user export success");
    }

    @Override
    public UserImportTotalDTO importInfo(String uuid) {
        UserImportTotalDTO vo = (UserImportTotalDTO) RedisUtils.get(importTotalKey(uuid));
        if (vo == null) {
            throw new BusinessException("miss_import_uuid", "找不到该次导入");
        }
        vo.setFailureNotes(redisTemplate.opsForList().range(importFailureKey(uuid), 0, vo.getFailure()));
        return vo;
    }

    private void insertUser(UserImportDTO dto) {
        UserAddQO qo = new UserAddQO();
        qo.setId(UUIDUtils.timeBasedStr());
        qo.setUserName(dto.getName());
        qo.setPhone(dto.getPhone());
        qo.setGender(0);
        qo.setLoginName(dto.getPhone());
//        qo.setPassword("Aa123!@#");
        userService.add(qo);
    }
}
