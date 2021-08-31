package com.bike.ztd.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bike.ztd.entity.TUser;
import com.baomidou.mybatisplus.service.IService;
import com.bike.ztd.qo.UserAddQO;
import com.bike.ztd.qo.UserQO;
import com.bike.ztd.vo.UserVO;

import java.util.HashMap;
import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
public interface TUserService extends IService<TUser> {
    /**
     * 通过登录名 查找用户
     *
     * @param name
     * @return
     */
    TUser findUserByLoginName(String name);

    /**
     * 根据用户id 查找用户
     *
     * @param userId
     * @return
     */
    TUser findUserByUserId(String userId);

    /**
     * 根据用户id查询手机号
     *
     * @param userId
     * @return
     */
    String findUserPhoneByUserId(String userId);

    /**
     * 用根据用户id查询户信息
     *
     * @param userId
     * @return
     */
    UserVO userInfo(String userId);

    /**
     * 根据手机号查找用户
     *
     * @param cellPhone
     */
    TUser getActiveCellphone(String cellPhone);

    /**
     * 删除用户
     *
     * @param userId
     */
    void deleteUserById(String userId);

    /**
     * 用户信息列表
     *
     * @param searchValue
     * @param current
     * @param size
     * @return
     */
    Page<UserVO> list(String searchValue, int current, int size);

    /**
     * 修改用户信息
     *
     * @param qo
     */
    void modify(UserQO qo);

    /**
     * 新增用户
     *
     * @param qo
     */
    String add(UserAddQO qo);

    /**
     * 查找用户手机
     *
     * @param userIds
     * @return
     */
    HashMap<String, String> findPhoneByUserIds(Set<String> userIds);


}
