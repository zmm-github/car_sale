package com.zmm.car.service.impl;

import com.zmm.car.entity.Customer;
import com.zmm.car.mapper.CustomerMapper;
import com.zmm.car.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zmm
 * @since 2023-05-25
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
