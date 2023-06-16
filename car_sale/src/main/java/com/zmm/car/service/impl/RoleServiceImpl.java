package com.zmm.car.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zmm.car.entity.Menu;
import com.zmm.car.entity.Role;
import com.zmm.car.entity.RoleMenu;
import com.zmm.car.mapper.MenuMapper;
import com.zmm.car.mapper.RoleMapper;
import com.zmm.car.mapper.RoleMenuMapper;
import com.zmm.car.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zmm
 * @since 2023-05-25
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional
    public void addRole(Role role) {
        // 写入角色表
        this.baseMapper.insert(role);

        // 写入角色菜单关系表
        if (role.getMenuIdList() != null && role.getMenuIdList().size() > 0) {
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    public Role getRoleById(Integer id) {
        // 获取指定角色
        Role role = this.baseMapper.selectById(id);

        // 获取角色ID对应的菜单ID列表
        List<Integer> menuIdList = roleMenuMapper.getMenuIdsByRoleId(id);
        role.setMenuIdList(menuIdList);

        return role;
    }

    @Override
    @Transactional
    public void updateRoleById(Role role) {
        // 1、修改角色
        this.baseMapper.updateById(role);
        // 2、删除原有的权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, role.getRoleId());
        roleMenuMapper.delete(wrapper);
        // 3.添加新的权限
        if (role.getMenuIdList() != null && role.getMenuIdList().size() > 0) {
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    @Transactional
    public void deleteRoleById(Integer id) {
        // 1、删除角色
        this.baseMapper.deleteById(id);
        // 2、删除权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, id);
        roleMenuMapper.delete(wrapper);
    }

    @Override
    public void deleteRoleByIds(List<Integer> ids) {
        // 1、删除角色
        this.baseMapper.deleteBatchIds(ids);
        // 2、删除权限，分别删除每个角色的权限
        for (Integer id : ids) {
            LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RoleMenu::getRoleId, id);
            roleMenuMapper.delete(wrapper);
        }
    }
}
