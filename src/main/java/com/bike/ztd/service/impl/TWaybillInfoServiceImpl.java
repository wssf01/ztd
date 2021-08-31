package com.bike.ztd.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bike.ztd.entity.TWaybillInfo;
import com.bike.ztd.enums.WaybillInfoEnum;
import com.bike.ztd.mapper.TWaybillInfoMapper;
import com.bike.ztd.service.TWaybillInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@Service
public class TWaybillInfoServiceImpl extends ServiceImpl<TWaybillInfoMapper, TWaybillInfo> implements TWaybillInfoService {
    @Override
    public List<TWaybillInfo> getInfoListByWaybillId(String waybillId) {
        EntityWrapper<TWaybillInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("waybill_id", waybillId);
        wrapper.orderBy("create_time", false);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public int getNumberCollectByWaybillId(String waybillId) {
        EntityWrapper<TWaybillInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("waybill_id", waybillId);
        wrapper.eq("waybill_type", WaybillInfoEnum.COLLECT);
        return baseMapper.selectCount(wrapper);
    }
}
