package com.zmm.car.service.impl;

import com.zmm.car.entity.Orders;
import com.zmm.car.mapper.OrdersMapper;
import com.zmm.car.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmm.car.utils.ColorUtil;
import com.zmm.car.vo.DataVO;
import com.zmm.car.vo.BarVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public BarVO getOrdersBar() {
        BarVO barVO = new BarVO();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<DataVO> values = new ArrayList<>();
        // 先查出所有订单数据
        List<Orders> orders = this.baseMapper.selectList(null);
        // 遍历统计各个车系的数量
        HashMap<String, Integer> amountMap = new HashMap<>();
        for (Orders order : orders) {
            if (amountMap.containsKey(order.getCarSeries())) {
                amountMap.put(order.getCarSeries(), amountMap.get(order.getCarSeries()) + 1);
            } else {
                amountMap.put(order.getCarSeries(), 1);
            }
        }

        // 遍历赋值
        for (Orders order : orders) {
            // 进行判断，如果数组中不包含此车系才进行添加
            if(!names.contains(order.getCarSeries())) {
                names.add(order.getCarSeries());
                DataVO dataVO = new DataVO();
                dataVO.setValue(amountMap.get(order.getCarSeries()));
                dataVO.setItemStyle(ColorUtil.createItemStyle(amountMap.get(order.getCarSeries())));
                values.add(dataVO);
            }
        }
        barVO.setNames(names);
        barVO.setValues(values);
        return barVO;
    }
}
