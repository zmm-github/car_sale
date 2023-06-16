package com.zmm.car.service.impl;

import com.zmm.car.entity.Orders;
import com.zmm.car.mapper.OrdersMapper;
import com.zmm.car.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zmm
 * @since 2023-06-13
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

}
