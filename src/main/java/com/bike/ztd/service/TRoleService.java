package com.bike.ztd.service;

import com.bike.ztd.entity.TRole;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
public interface TRoleService extends IService<TRole> {

    /**
     * 通过角色id查找出对应所有的用户具备角色名称
     * @param ids
     * @return
     */
    List<String> findRoleNameByRoleIds(List<String> ids);
}
