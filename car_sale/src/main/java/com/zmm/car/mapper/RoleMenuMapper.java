package com.zmm.car.mapper;

import com.zmm.car.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zmm
 * @since 2023-06-06
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    List<Integer> getMenuIdsByRoleId(Integer id);
}
