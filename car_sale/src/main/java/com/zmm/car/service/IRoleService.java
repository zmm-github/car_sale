package com.zmm.car.service;

import com.zmm.car.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zmm
 * @since 2023-05-25
 */
public interface IRoleService extends IService<Role> {

    void addRole(Role role);

    Role getRoleById(Integer id);

    void updateRoleById(Role role);

    void deleteRoleById(Integer id);

    void deleteRoleByIds(List<Integer> ids);
}
