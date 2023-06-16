package com.zmm.car.controller;

import com.zmm.car.common.vo.ResultMap;
import com.zmm.car.entity.Menu;
import com.zmm.car.service.IMenuService;
//import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zmm
 * @since 2023-06-05
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation("查询所有菜单列表")
    @GetMapping("/getMenuList")
    public ResultMap<List<Menu>> getMenuList() {
        List<Menu> menuList = menuService.getMenuList();
        if (menuList.size()>0 && menuList != null) {
            return ResultMap.success("获取所有菜单列表成功...", menuList);
        }
        return ResultMap.fail("获取所有菜单列表失败...");
    }

}
