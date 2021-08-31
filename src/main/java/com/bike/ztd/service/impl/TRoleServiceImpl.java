package com.bike.ztd.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bike.ztd.entity.TRole;
import com.bike.ztd.mapper.TRoleMapper;
import com.bike.ztd.service.TRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@Service
public class TRoleServiceImpl extends ServiceImpl<TRoleMapper, TRole> implements TRoleService {

    @Override
    public List<String> findRoleNameByRoleIds(List<String> ids) {
        EntityWrapper<TRole> wrapper = new EntityWrapper<>();
        wrapper.in("pk_id",ids);
        List<TRole> roleList = baseMapper.selectList(wrapper);
        return roleList.stream().map(TRole::getRoleName).collect(Collectors.toList());
    }

}
