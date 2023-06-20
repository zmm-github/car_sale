package com.zmm.car.service;

import com.zmm.car.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zmm.car.vo.BarVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zmm
 * @since 2023-06-13
 */
public interface IOrdersService extends IService<Orders> {

    BarVO getOrdersBar();
}
