package com.zmm.car.mapper;

import com.zmm.car.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zmm
 * @since 2023-06-05
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getMenuListByUserId(@Param("userId") Integer userId, @Param("parentId") Integer parentId);
}
