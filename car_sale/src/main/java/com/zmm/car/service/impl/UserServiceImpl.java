package com.zmm.car.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zmm.car.entity.Menu;
import com.zmm.car.entity.User;
import com.zmm.car.entity.UserRole;
import com.zmm.car.mapper.UserMapper;
import com.zmm.car.mapper.UserRoleMapper;
import com.zmm.car.service.IMenuService;
import com.zmm.car.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmm.car.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zmm
 * @since 2023-06-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IMenuService menuService;

    /**
     * 对加密过的密码进行登录
     *
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> login(User user) {
        // 根据用户名和密码查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User loginUser = this.baseMapper.selectOne(wrapper);
        // 结果不为空，并且密码和传入密码匹配，则生成token，并将用户信息存入redis中
        if (loginUser != null && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())) {
            // 暂时使用UUID，终极方案使用jwt
            // String key = "user:" + UUID.randomUUID();

            // 存入redis
            // 将密码置为空，不应该将密码存入redis中
            loginUser.setPassword(null);
            // redisTemplate.opsForValue().set(key, loginUser, 60, TimeUnit.MINUTES);

            // 创建jwt
            String token = jwtUtil.createToken(loginUser);

            // 返回数据
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", token);
            return data;
        }
        return null;
    }

//    @Override
//    public Map<String, Object> login(User user) {
//        // 根据用户名和密码查询
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getUsername, user.getUsername());
//        wrapper.eq(User::getPassword, user.getPassword());
//        User loginUser = this.baseMapper.selectOne(wrapper);
//        // 结果不为空，则生成token，并将用户信息存入redis中
//        if (loginUser != null){
//            // 暂时使用UUID，终极方案使用jwt
//            String key = "user:" + UUID.randomUUID();
//
//            // 存入redis
//            // 将密码置为空，不应该将密码存入redis中
//            loginUser.setPassword(null);
//            redisTemplate.opsForValue().set(key, loginUser, 60, TimeUnit.MINUTES);
//
//            // 返回数据
//            HashMap<String, Object> data = new HashMap<>();
//            data.put("token", key);
//            return data;
//        }
//        return null;
//    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        // 根据token获取用户信息，redis
        // Object obj = redisTemplate.opsForValue().get(token);
        User loginUser = null;
        try {
            loginUser = jwtUtil.parseToken(token, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("----------------" + obj + "-------------");
        if (loginUser != null) {
            // 获取登录用户信息
            // User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", loginUser.getUsername());
            data.put("avatar", loginUser.getAvatar());

            // 保存登录用户信息
            data.put("loginUser", loginUser);

            // 角色
            List<String> roleList = this.baseMapper.getRoleNameByUserId(loginUser.getId());
            data.put("roles", roleList);

            // 权限列表
            List<Menu> menuList = menuService.getMenuListByUserId(loginUser.getId());
            data.put("menuList", menuList);

            // 返回数据
            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
//        redisTemplate.delete(token);
    }

    @Override
    @Transactional
    public void addUser(User user) {
        // 1、新增用户
        this.baseMapper.insert(user);
        // 2、新增用户角色
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null && roleIdList.size() > 0) {
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }

    @Override
    @Transactional
    public void updateUserById(User user) {
        // 1、修改用户
        this.baseMapper.updateById(user);
        // 2、删除原有角色
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, user.getId());
        userRoleMapper.delete(queryWrapper);
        // 3、新增新的角色
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null && roleIdList.size() > 0) {
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }

    @Override
    @Transactional
    public User getUserById(Integer id) {
        // 1、获取用户信息
        User user = this.baseMapper.selectById(id);
        // 2、获取用户对应的角色信息
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, id);
        List<UserRole> userRoleList = userRoleMapper.selectList(queryWrapper);
        List<Integer> roleIdList = userRoleList.stream()
                                            .map(userRole -> {return userRole.getRoleId();})
                                            .collect(Collectors.toList());
        user.setRoleIdList(roleIdList);
        return user;
    }

    @Override
    @Transactional
    public void deleteUserById(Integer id) {
        // 1、删除用户
        this.baseMapper.deleteById(id);
        // 2、删除用户对应的角色
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, id);
        userRoleMapper.delete(queryWrapper);
    }

    @Override
    public void register(User user) {
        // 1、注册用户
        this.baseMapper.insert(user);
        // 2、注册的用户默认为普通用户
        userRoleMapper.insert(new UserRole(null, user.getId(), 2));
    }
}