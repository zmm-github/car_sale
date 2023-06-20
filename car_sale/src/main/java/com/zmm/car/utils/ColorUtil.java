package com.zmm.car.utils;/**
 * @author 阿毛
 * @date 2023/6/18 11:49
 */

import java.util.HashMap;
import java.util.Map;

/**
 * @program: car_sale
 * @description: 根据传递过来的值，自动分配颜色
 * @author: Try harder
 * @create: 2023-06-18 11:49
 * @version:1.0
 **/
public class ColorUtil {
    public static Map<String, String> createItemStyle(Integer amount) {
        String color = "";
        if (amount < 2) {
            color = "#96dee8";
        } else if (amount < 3) {
            color = "#c4ebad";
        } else if (amount < 4) {
            color = "#6be6c1";
        } else if (amount < 5) {
            color = "#3fb1e3";
        } else if (amount < 6) {
            color = "#a0a7e6";
        } else if (amount < 7) {
            color = "#ea7ccc";
        } else {
            color = "#5470c6";
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("color", color);
        return map;
    }
}