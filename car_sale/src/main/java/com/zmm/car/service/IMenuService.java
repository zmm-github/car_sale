package com.zmm.car.service;

import com.zmm.car.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zmm
 * @since 2023-06-05
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> getMenuList();

    List<Menu> getMenuListByUserId(Integer userId);
}
