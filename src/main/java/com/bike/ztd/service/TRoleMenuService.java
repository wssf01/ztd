package com.bike.ztd.service;

import com.bike.ztd.entity.TRoleMenu;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色权限表 服务类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
public interface TRoleMenuService extends IService<TRoleMenu> {
    /**
     * 通过角色id列表查找出所有对应的菜单id
     * @param roleIds
     * @return
     */
    List<String> findMenuIdsByRoleIds(Set<String> roleIds);
}
