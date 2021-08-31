package com.bike.ztd.mapper;

import com.bike.ztd.entity.TWaybillInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
public interface TWaybillInfoMapper extends BaseMapper<TWaybillInfo> {

    void insertBatch(@Param("list") List<TWaybillInfo> infoList);
}
