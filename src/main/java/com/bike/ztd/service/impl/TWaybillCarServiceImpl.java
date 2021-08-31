package com.bike.ztd.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bike.ztd.entity.TWaybillCar;
import com.bike.ztd.enums.WaybillInfoEnum;
import com.bike.ztd.mapper.TWaybillCarMapper;
import com.bike.ztd.service.TWaybillCarService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
@Service
public class TWaybillCarServiceImpl extends ServiceImpl<TWaybillCarMapper, TWaybillCar> implements TWaybillCarService {

    @Override
    public void deleteByWaybillId(String waybillId, WaybillInfoEnum waybillType) {
        EntityWrapper<TWaybillCar> wrapper = new EntityWrapper<>();
        wrapper.eq("waybill_id", waybillId);
        if (waybillType != null) {
            wrapper.eq("waybill_type", waybillType);
        }
        baseMapper.delete(wrapper);
    }

    @Override
    public int getNumberCollectByWaybillId(String waybillId) {
        EntityWrapper<TWaybillCar> wrapper = new EntityWrapper<>();
        wrapper.eq("waybill_id", waybillId);
        wrapper.eq("waybill_type", WaybillInfoEnum.COLLECT);
        List<TWaybillCar> carList = baseMapper.selectList(wrapper);
        int number = 0;
        if (CollectionUtils.isEmpty(carList)) {
            return number;
        }
        //计算
        for (TWaybillCar car : carList) {
            String carNumber = car.getCarNumber();
            if (StringUtils.isNotEmpty(carNumber)) {
                List<String> carNumberList = Arrays.asList(carNumber.split(","));
                number = number + carNumberList.size();
            }
        }
        return number < 0 ? 0 : number;
    }

    @Override
    public List<TWaybillCar> getCollectCarListByWaybillId(String waybillId) {
        EntityWrapper<TWaybillCar> wrapper = new EntityWrapper<>();
        wrapper.eq("waybill_id", waybillId);
        wrapper.eq("waybill_type", WaybillInfoEnum.COLLECT);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<TWaybillCar> getCarListByWaybillId(String waybillId) {
        EntityWrapper<TWaybillCar> wrapper = new EntityWrapper<>();
        wrapper.eq("waybill_id", waybillId);
        return baseMapper.selectList(wrapper);
    }
}
