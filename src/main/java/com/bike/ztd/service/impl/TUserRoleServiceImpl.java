package com.bike.ztd.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bike.ztd.entity.TUserRole;
import com.bike.ztd.mapper.TUserRoleMapper;
import com.bike.ztd.service.TUserRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@Service
public class TUserRoleServiceImpl extends ServiceImpl<TUserRoleMapper, TUserRole> implements TUserRoleService {
    @Override
    public List<String> findRoleIdsByUserId(String userId) {
        EntityWrapper<TUserRole> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        List<TUserRole> roleList = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(roleList)){
            return Lists.newArrayList();
        }
        return roleList.stream().map(TUserRole::getRoleId).collect(Collectors.toList());
    }
}
