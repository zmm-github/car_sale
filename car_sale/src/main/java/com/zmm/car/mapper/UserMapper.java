package com.zmm.car.mapper;

import com.zmm.car.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zmm
 * @since 2023-06-03
 */
public interface UserMapper extends BaseMapper<User> {
    List<String> getRoleNameByUserId(Integer userId);
}
