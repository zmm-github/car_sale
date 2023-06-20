package com.zmm.car.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @program: car_sale
 * @description: 折线图实体类
 * @author: Try harder
 * @create: 2023-06-19 10:20
 * @version:1.0
 **/
@Data
public class LineVO {
    private List<String> dateList;
    private List<Integer> values;
}