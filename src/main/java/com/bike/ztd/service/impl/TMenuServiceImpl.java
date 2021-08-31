package com.bike.ztd.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bike.ztd.entity.TMenu;
import com.bike.ztd.mapper.TMenuMapper;
import com.bike.ztd.service.TMenuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@Service
public class TMenuServiceImpl extends ServiceImpl<TMenuMapper, TMenu> implements TMenuService {
    @Override
    public List<String> findPermsByMenuIds(List<String> menuIds) {
        EntityWrapper<TMenu> wrapper = new EntityWrapper<>();
        wrapper.in("pk_id",menuIds);
        return baseMapper.selectList(wrapper).stream().map(TMenu::getPerms).collect(Collectors.toList());
    }
}
