package com.zmm.car.vo;

import lombok.Data;

import java.util.Map;

/**
 * @program: car_sale
 * @description: 订单数据VO
 * @author: Try harder
 * @create: 2023-06-18 10:24
 * @version:1.0
 **/
@Data
public class DataVO {
    private Integer value;
    private Map<String, String> itemStyle;
}