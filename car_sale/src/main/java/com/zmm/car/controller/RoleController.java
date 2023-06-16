package com.zmm.car.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmm.car.common.vo.ResultMap;
import com.zmm.car.entity.Role;
import com.zmm.car.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    /* 获取角色列表 */
    @GetMapping("/getRoleList")
    public ResultMap<Map<String, Object>> getRoleList(@RequestParam(value = "roleName", required = false) String roleName,
                                                      @RequestParam("currentPage") Long currentPage,
                                                      @RequestParam("pageSize") Long pageSize) {

        // 设置查询条件
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasLength(roleName), Role::getRoleName, roleName);
        queryWrapper.orderByDesc(Role::getRoleId); // 根据RoleId排序

        // 设置分页查询
        Page<Role> page = new Page<>(currentPage, pageSize);

        // 进行分页查询
        roleService.page(page, queryWrapper);

        // 设置返回给前端的内容
        HashMap<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        return ResultMap.success(data);
    }

    /* 新增角色 */
    @PostMapping("/addRole")
    public ResultMap addRole(@RequestBody Role role) {
        roleService.addRole(role);
        return ResultMap.success("新增角色成功...");
    }

    /* 根据ID修改角色 */
    @PutMapping("/updateRoleById")
    public ResultMap updateRoleById(@RequestBody Role role) {
        roleService.updateRoleById(role);
        return ResultMap.success("修改角色成功...");
    }

    /* 根据ID获取指定角色 */
    @GetMapping("/getRoleById/{id}")
    public ResultMap<Role> getRoleById(@PathVariable("id") Integer id) {
        Role role = roleService.getRoleById(id);
        if (role != null) {
            return ResultMap.success(role);
        }
        return ResultMap.fail("获取角色失败...");
    }

    /* 根据ID删除指定角色 */
    @DeleteMapping("/deleteRoleById/{id}")
    public ResultMap deleteRoleById(@PathVariable("id") Integer id) {
        roleService.deleteRoleById(id);
        return ResultMap.success("已删除编号 “" + id + "” 的角色...");
    }

    /* 根据id批量删除角色 */
    @DeleteMapping("/deleteRoleByIds")
    public ResultMap deleteRoleByIds(@RequestBody List<Integer> ids) {
        roleService.deleteRoleByIds(ids);
        return ResultMap.success("已删除编号 “" + ids + "” 的角色...");
    }

    /* 查询所有角色 */
    @GetMapping("/getAllRoleList")
    public ResultMap<List<Role>> getAllRoleList() {
        List<Role> roleList = roleService.list();
        return ResultMap.success(roleList);
    }
}
