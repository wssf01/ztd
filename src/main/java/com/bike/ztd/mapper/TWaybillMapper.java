package com.bike.ztd.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.bike.ztd.entity.TWaybill;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
public interface TWaybillMapper extends BaseMapper<TWaybill> {

    List<TWaybill> pageList(Map<String, Object> map, @Param("pagination") Pagination pagination);

    List<TWaybill> listExport(Map<String, Object> map);

    //void insert(Waybill waybill);
}
