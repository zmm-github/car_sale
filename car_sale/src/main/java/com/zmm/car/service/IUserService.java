package com.zmm.car.service;

import com.zmm.car.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zmm
 * @since 2023-06-03
 */
public interface IUserService extends IService<User> {
    /* 用户登录 */
    Map<String, Object> login(User user);

    /* 获取登录用户信息 */
    Map<String, Object> getUserInfo(String token);

    void logout(String token);

    void addUser(User user);

    void updateUserById(User user);

    User getUserById(Integer id);

    void deleteUserById(Integer id);

    void register(User user);
}
