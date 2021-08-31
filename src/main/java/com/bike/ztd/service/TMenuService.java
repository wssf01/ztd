package com.bike.ztd.service;

import com.bike.ztd.entity.TMenu;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
public interface TMenuService extends IService<TMenu> {
    /**
     * 通过菜单id列表获得权限列表
     * @param menuIds
     * @return
     */
    List<String> findPermsByMenuIds(List<String> menuIds);
}
