package com.bike.ztd.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.bike.ztd.entity.TUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
public interface TUserMapper extends BaseMapper<TUser> {

    List<TUser> list(Map<String, Object> map, @Param("pagination") Pagination page);

}
