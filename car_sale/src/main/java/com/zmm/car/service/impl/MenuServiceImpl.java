package com.zmm.car.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zmm.car.entity.Menu;
import com.zmm.car.mapper.MenuMapper;
import com.zmm.car.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zmm
 * @since 2023-06-05
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> getMenuList() {
        // 一级菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, 0);
        List<Menu> menuList = this.list(wrapper);
        setMenuChildren(menuList);
        return menuList;
    }

    private void setMenuChildren(List<Menu> menuList) {
        // 判断一级菜单列表是否为空
        if (menuList != null && menuList.size() > 0) {
            // 填充子菜单
            for (Menu menu : menuList) {
                LambdaQueryWrapper<Menu> subWrapper = new LambdaQueryWrapper<>();
                subWrapper.eq(Menu::getParentId, menu.getMenuId());
                List<Menu> subMenuList = this.list(subWrapper);
                menu.setChildren(subMenuList);
                // 递归
                setMenuChildren(subMenuList);
            }
        }
    }

    @Override
    public List<Menu> getMenuListByUserId(Integer userId) {
        // 一级菜单
        List<Menu> menuList = this.baseMapper.getMenuListByUserId(userId, 0);
        // 子菜单
        setMenuChildrenByUserId(userId, menuList);

        return menuList;
    }

    private void setMenuChildrenByUserId(Integer userId, List<Menu> menuList) {
        if (menuList != null && menuList.size() > 0) {
            for (Menu menu : menuList) {
                List<Menu> subMenuList = this.baseMapper.getMenuListByUserId(userId, menu.getMenuId());
                menu.setChildren(subMenuList);
                // 递归
                setMenuChildrenByUserId(userId, subMenuList);
            }
        }
    }

}
