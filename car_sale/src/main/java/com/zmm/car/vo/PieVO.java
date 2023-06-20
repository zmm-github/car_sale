package com.zmm.car.vo;

import lombok.Data;

import java.util.Map;

/**
 * @program: car_sale
 * @description: 饼图实体数据
 * @author: Try harder
 * @create: 2023-06-19 09:47
 * @version:1.0
 **/
@Data
public class PieVO {
    private Integer value;
    private String name;
    private Map<String, String> itemStyle;
}