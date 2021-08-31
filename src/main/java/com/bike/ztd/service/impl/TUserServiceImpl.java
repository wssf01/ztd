package com.bike.ztd.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bike.ztd.entity.TUser;
import com.bike.ztd.enums.UserStatus;
import com.bike.ztd.exception.BusinessException;
import com.bike.ztd.mapper.TUserMapper;
import com.bike.ztd.qo.UserAddQO;
import com.bike.ztd.qo.UserQO;
import com.bike.ztd.service.OSSService;
import com.bike.ztd.service.TUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bike.ztd.util.SystemDefine;
import com.bike.ztd.util.UUIDUtils;
import com.bike.ztd.vo.UserVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.bike.ztd.util.JWTUtils.getUserIdByToken;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@Slf4j
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {
    @Autowired
    private OSSService ossService;

    @Override
    public TUser findUserByLoginName(String name) {
        TUser user = new TUser();
        user.setLoginName(name);
        user = baseMapper.selectOne(user);
        if (user != null) {
            if (StringUtils.isNotEmpty(user.getAvatar())) {
                user.setAvatar(ossService.downLoad(user.getAvatar()));
            }
        }
        return user;
    }

    @Override
    public TUser findUserByUserId(String userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public String findUserPhoneByUserId(String userId) {
        TUser user = findUserByUserId(userId);
        if (user == null) {
            log.error("user not exist:" + userId);
            return "";
        }
        return user.getPhone();
    }

    @Override
    public UserVO userInfo(String userId) {
        if (StringUtils.isEmpty(userId)) {
            userId = getUserIdByToken();
        }
        UserVO vo = new UserVO();
        TUser user = findUserByUserId(userId);
        BeanUtils.copyProperties(user, vo);
        if (StringUtils.isNotEmpty(vo.getAvatar())) {
            vo.setAvatar(ossService.downLoad(vo.getAvatar()));
        }
        return vo;
    }

    @Override
    public TUser getActiveCellphone(String cellPhone) {
        TUser user = new TUser();
        user.setPhone(cellPhone);
        return baseMapper.selectOne(user);
    }

    @Override
    public void deleteUserById(String userId) {
        TUser user = findUserByUserId(userId);
        if (user == null) {
            throw new BusinessException("user_not_exist", "用户不存在");
        }
        user.setUserStatus(UserStatus.DELETE);
        baseMapper.updateById(user);
        log.info("user delete :" + user.getPhone());
    }

    @Override
    public Page<UserVO> list(String searchValue, int current, int size) {
        //组装查询条件
        Map<String, Object> map = Maps.newHashMap();
        map.put("searchValue", searchValue);
        //查询相关信息
        Page<TUser> page = new Page<>(current, Math.min(size, SystemDefine.MAX_PAGE_SIZE));
        List<TUser> list = this.baseMapper.list(map, page);
        Page<UserVO> resultPage = new Page<>(current, Math.min(size, SystemDefine.MAX_PAGE_SIZE));
        List<UserVO> resultList = Lists.newArrayList();
        list.forEach(user -> {
            //运单信息补充
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);
            resultList.add(vo);
        });
        resultPage.setRecords(resultList);
        resultPage.setTotal(page.getTotal());
        return resultPage;
    }

    @Override
    public void modify(UserQO qo) {
        TUser user = findUserByUserId(qo.getId());
        if (user == null) {
            throw new BusinessException("user_not_exist", "用户不存在");
        }
        if (qo.getStatus() != null) {
            user.setUserStatus(qo.getStatus());
        }
        if (qo.getAge() != null) {
            user.setAge(qo.getAge());
        }
        if (StringUtils.isNotEmpty(qo.getAvatar())) {
            user.setAvatar(qo.getAvatar());
        }
        if (StringUtils.isNotEmpty(qo.getEmail())) {
            user.setEmail(qo.getEmail());
        }
        if (qo.getGender() != null) {
            user.setGender(qo.getGender());
        }
        if (StringUtils.isNotEmpty(qo.getIdentityCard())) {
            user.setIdentityCard(qo.getIdentityCard());
        }
        if (StringUtils.isNotEmpty(qo.getPhone())) {
            user.setPhone(qo.getPhone());
        }
        if (StringUtils.isNotEmpty(qo.getUserName())) {
            user.setUserName(qo.getUserName());
        }
        baseMapper.updateById(user);
        log.info("user update success");
    }

    @Override
    public String add(UserAddQO qo) {
        //验证手机号是否已存在
        TUser user = getActiveCellphone(qo.getPhone());
        if (user != null) {
            if (UserStatus.DELETE.equals(user.getUserStatus())) {
                user.setUserStatus(UserStatus.NORMAL);
                baseMapper.updateById(user);
                log.info("user add success for update");
                return user.getPkId();
            } else {
                throw new BusinessException("user_phone_exist", "已存在相同手机号的用户");
            }
        }
        if (StringUtils.isEmpty(qo.getLoginName())) {
            qo.setLoginName(qo.getPhone());
        } else {
            user = findUserByLoginName(qo.getUserName());
            if (user != null) {
                throw new BusinessException("user_login_name_exist", "已存在相同登录名的用户");
            }
        }
        if (StringUtils.isEmpty(qo.getPassword())) {
            qo.setPassword(qo.getPassword());
        }
        user = new TUser();
        BeanUtils.copyProperties(qo, user);
        user.setPkId(UUIDUtils.timeBasedStr());
        user.setUserStatus(UserStatus.NORMAL);
        baseMapper.insert(user);
        log.info("user add success");
        return user.getPkId();
    }

    @Override
    public HashMap<String, String> findPhoneByUserIds(Set<String> userIds) {
        HashMap<String, String> map = Maps.newHashMap();
        if (CollectionUtils.isEmpty(userIds)) {
            return map;
        }
        EntityWrapper<TUser> wrapper = new EntityWrapper<>();
        wrapper.in("id", userIds);
        List<TUser> list = baseMapper.selectList(wrapper);
        list.forEach(user -> {
            map.put(user.getPkId(), user.getPhone());
        });
        return map;
    }

}
