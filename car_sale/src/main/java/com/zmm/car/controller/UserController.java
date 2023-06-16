package com.zmm.car.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmm.car.common.vo.ResultMap;
import com.zmm.car.entity.User;
import com.zmm.car.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zmm
 * @since 2023-06-02
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* 登录 */
    @PostMapping("/login")
    public ResultMap<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> data = userService.login(user);
        if (data != null) {
            return ResultMap.success("登录成功，正在跳转...", data);
        }
        return ResultMap.fail(20002, "用户名或密码错误");
    }

    /* 获取登录用户信息 */
    @GetMapping("/info")
    public ResultMap<Map<String, Object>> getUserInfo(@RequestParam("token") String token) {
        // 根据token获取用户信息，redis
        Map<String, Object> data = userService.getUserInfo(token);
        if (data != null) {
            return ResultMap.success(data);
        }
        return ResultMap.fail(20003, "登录信息无效，请重新登录");
    }

    /* 退出登录 */
    @PostMapping("/logout")
    public ResultMap<?> logout(@RequestHeader("X-Token") String token) {
        userService.logout(token);
        return ResultMap.success("退出成功");
    }

    /* 获取所有用户信息 */
    @GetMapping("/getAllUser")
    public ResultMap<List<User>> getAllSysUser() {
        List<User> userList = userService.list();
        return ResultMap.success("查询成功", userList);
    }

    /* 获取所有用户信息 */
    @GetMapping("/getUserList")
    public ResultMap<Map<String, Object>> getUserList(@RequestParam(value = "nickName", required = false) String nickName,
                                                      @RequestParam(value = "status", required = false) Integer status,
                                                      @RequestParam("currentPage") Long currentPage,
                                                      @RequestParam("pageSize") Long pageSize) {
        // 设置模糊查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(nickName), User::getNickName, nickName);
        queryWrapper.like(status != null, User::getStatus, status);

        // 设置分页查询条件
        Page<User> page = new Page<>(currentPage, pageSize);

        // 进行分页模糊查询
        userService.page(page, queryWrapper);

        // 设置返回给前端的内容
        HashMap<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        return ResultMap.success(data);
    }

    // 新增用户
    @PostMapping("/addUser")
    public ResultMap addUser(@RequestBody User user) {
        // 对存储到数据库中的密码进行加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 添加到数据库中
        userService.addUser(user);
        return ResultMap.success("新增用户成功...");
    }

    // 根据id删除指定用户
    @DeleteMapping("/deleteUserById/{id}")
    public ResultMap deleteUserById(@PathVariable("id") Integer id) {
        userService.deleteUserById(id);
        return ResultMap.success("已删除编号 “" + id + "” 的用户..");
    }

    // 根据id获取指定用户信息
    @GetMapping("/getUserById/{id}")
    public ResultMap<User> getUserById(@PathVariable("id") Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResultMap.success("已成功获取编号 “" + id + "” 的用户信息", user);
        }

        return ResultMap.fail("获取用户失败...");
    }

    // 根据id修改用户
    @PutMapping("/updateUserById")
    public ResultMap updateUserById(@RequestBody User user) {
        userService.updateUserById(user);
        return ResultMap.success("修改用户成功...");
    }

    // 根据id批量删除用户
    @DeleteMapping("/deleteUserByIds")
    public ResultMap deleteUserByIds(@RequestBody List<Integer> ids) {
        boolean b = userService.removeByIds(ids);
        if (b) {
            return ResultMap.success("已删除编号 “" + ids + "” 的用户...");
        }

        return ResultMap.fail("删除编号 “" + ids + "” 的用户失败...");
    }

    // 用户注册
    @PostMapping("/register")
    public ResultMap register(@RequestBody User user) {
        // 对存储到数据库中的密码进行加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 注册的用户默认都是正常的
        user.setStatus("1");
        userService.register(user);
        return ResultMap.success("注册用户成功，赶快去登录吧！");
    }

    // 根据用户名查询用户信息
    @GetMapping("/getUserByUsername")
    public ResultMap<User> getUserByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User one = userService.getOne(queryWrapper);
        return ResultMap.success(one);
    }

    // 根据用户ID修改用户密码和头像
    @PutMapping("/updateUserPassById")
    public ResultMap updateUserPassById(@RequestBody User user) {
        if (user.getPassword() != null && user.getPassword() != "") {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.updateById(user);
        return ResultMap.success("修改密码成功，请重新登录！");
    }

    // 根据用户ID修改头像
    @PutMapping("/updateUserAvatarById")
    public ResultMap updateUserAvatarById(@RequestBody User user) {
        userService.updateById(user);
        return ResultMap.success("修改头像成功，请重新登录！");
    }

}