//package com.bike.ztd;
//
//import com.baomidou.mybatisplus.plugins.Page;
//import com.bike.ztd.entity.TUser;
//import com.bike.ztd.mapper.TUserMapper;
//import com.bike.ztd.service.TUserService;
//import org.crazycake.shiro.RedisManager;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
///**
// * 简单的测试增删改查
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class MybatisplusSpringbootApplicationTests {
//
//
//    @Autowired
//    private TUserMapper userMapper;
//
//    @Autowired
//    private TUserService userService;
//
//    @Test
//    public void addUser() {
//        TUser user = new TUser();
//
//        user.setAge(200);
//        user.setEmail("belonghuang@outlook.com");
//        user.setLoginName("belongHuang");
//        userMapper.insert(user);
//    }
//
//    @Test
//    public void updateUser() {
//        TUser user = userMapper.selectById(1);
//        user.setAge(300);
//        int i = userMapper.updateById(user);
//        if (i>0) {
//            System.out.println("修改成功");
//        }else {
//            System.out.println("修改失败");
//        }
//    }
//
//    @Test
//    public void deleteUser() {
//        int i = userMapper.deleteById(1);
//        if (i>0) {
//            System.out.println("删除成功");
//        }else {
//            System.out.println("删除失败");
//        }
//    }
//    @Test
//    public void selectUser() {
//        List<TUser> users = userMapper.selectList(null);
//        users.stream().map(TUser::getLoginName).forEach(System.out::println);//打印出所有用户名字
//    }
//
//    @Test
//    public void selectOne(){
//        TUser jone = userService.findUserByLoginName("Jone");
//        System.out.println(jone.getPassword());
//    }
//
//    @Autowired
//    RedisManager redisManager;
//
//    @Test
//    public void redis(){
//        System.out.println(redisManager.getHost());
//        System.out.println();
//    }
//}
