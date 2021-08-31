package com.bike.ztd.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bike.ztd.entity.TRoleMenu;
import com.bike.ztd.mapper.TRoleMenuMapper;
import com.bike.ztd.service.TRoleMenuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@Service
public class TRoleMenuServiceImpl extends ServiceImpl<TRoleMenuMapper, TRoleMenu> implements TRoleMenuService {
    @Override
    public List<String> findMenuIdsByRoleIds(Set<String> roleIds) {
        EntityWrapper<TRoleMenu> wrapper = new EntityWrapper<>();
        wrapper.in("role_id",roleIds);
        List<TRoleMenu> roleMenuList = baseMapper.selectList(wrapper);
        return roleMenuList.stream().map(TRoleMenu::getMenuId).collect(Collectors.toList());
    }
}
