package com.bike.ztd.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bike.ztd.entity.TWaybillCar;
import com.bike.ztd.entity.TWaybillPhoto;
import com.bike.ztd.enums.WaybillInfoEnum;
import com.bike.ztd.mapper.TWaybillPhotoMapper;
import com.bike.ztd.service.OSSService;
import com.bike.ztd.service.TWaybillPhotoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@Slf4j
@Service
public class TWaybillPhotoServiceImpl extends ServiceImpl<TWaybillPhotoMapper, TWaybillPhoto> implements TWaybillPhotoService {
    @Autowired
    private OSSService ossService;

    @Override
    public void deleteByWaybillId(String waybillId, WaybillInfoEnum type) {
        EntityWrapper<TWaybillPhoto> wrapper = new EntityWrapper<>();
        wrapper.eq("waybill_id", waybillId);
        if (type != null) {
            wrapper.eq("waybill_type", type);
        }
        List<TWaybillPhoto> waybillInfoList = baseMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(waybillInfoList)) {
            waybillInfoList.forEach(waybillInfo -> {
                if (StringUtils.isNotEmpty(waybillInfo.getPhoto())) {
                    try {
                        ossService.delete(waybillInfo.getPhoto());
                    } catch (Exception e) {
                        log.error("photo delete error :" + waybillInfo.getPhoto());
                    }
                }
            });
            baseMapper.delete(wrapper);
        }
    }

    @Override
    public List<TWaybillPhoto> selectListByWaybillId(String waybillId) {
        EntityWrapper<TWaybillPhoto> wrapper = new EntityWrapper<>();
        wrapper.eq("waybill_id", waybillId);
        List<TWaybillPhoto> list = baseMapper.selectList(wrapper);
        list.forEach(waybillPhoto -> {
            if (StringUtils.isNotEmpty(waybillPhoto.getPhoto())) {
                waybillPhoto.setPhoto(ossService.downLoad(waybillPhoto.getPhoto()));
            }
        });
        return list;
    }
}
