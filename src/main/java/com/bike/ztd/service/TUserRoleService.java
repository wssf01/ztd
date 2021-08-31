package com.bike.ztd.service;

import com.bike.ztd.entity.TUserRole;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
public interface TUserRoleService extends IService<TUserRole> {
    /**
     * 通过用户id 查找出具备的角色id
     * @param userId
     * @return
     */
    List<String> findRoleIdsByUserId(String userId);
}
