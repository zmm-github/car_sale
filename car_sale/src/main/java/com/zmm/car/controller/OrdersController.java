package com.zmm.car.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmm.car.common.vo.ResultMap;
import com.zmm.car.entity.Orders;
import com.zmm.car.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zmm
 * @since 2023-06-13
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;

    @GetMapping("/getOrdersList")
    public ResultMap<Map<String, Object>> getOrdersList(@RequestParam(value = "name", required = false) String name,
                                                             @RequestParam(value = "saleName", required = false) String saleName,
                                                             @RequestParam(value = "status", required = false) String status,
                                                             @RequestParam("currentPage") Long currentPage,
                                                             @RequestParam("pageSize") Long pageSize) {

        // 设置模糊查询条件
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(name), Orders::getName, name);
        queryWrapper.like(StringUtils.hasLength(saleName), Orders::getSaleName, saleName);
        queryWrapper.like(StringUtils.hasLength(status), Orders::getStatus, status);

        // 设置分页查询
        Page<Orders> page = new Page<>(currentPage, pageSize);
        // 进行分页模糊查询
        ordersService.page(page, queryWrapper);

        // 向前端返回数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("rows", page.getRecords());

        // 返回
        return ResultMap.success(map);
    }

    /* 新增订单 */
    @PostMapping("/addOrders")
    public ResultMap addOrders(@RequestBody Orders orders) throws ParseException {
        Date date = new Date();
        orders.setCreateTime(date);
        orders.setUpdateTime(date);
        ordersService.save(orders);
        return ResultMap.success("新增订单成功！！！");
    }

    /* 修改订单 */
    @PutMapping("/updateOrders")
    public ResultMap updateOrders(@RequestBody Orders orders) {
        Date date = new Date();
        orders.setUpdateTime(date);
        ordersService.updateById(orders);
        return ResultMap.success("修改订单成功！！！");
    }

    /* 删除订单 */
    @DeleteMapping("/deleteOrdersById/{id}")
    public ResultMap deleteOrdersById(@PathVariable("id") Integer id) {
        ordersService.removeById(id);
        return ResultMap.success("删除订单成功！！！");
    }

    /* 删除选中的多个订单 */
    @DeleteMapping("/deleteOrdersByIds")
    public ResultMap deleteOrdersByIds(@RequestBody List<Integer> ids) {
        ordersService.removeByIds(ids);
        return ResultMap.success("已成功删除编号 “" + ids + "” 的订单！！！");
    }

    /* 根据id查询指定订单信息 */
    @GetMapping("/getOrdersById/{id}")
    public ResultMap<Orders> getOrdersById(@PathVariable("id") Integer id) {
        Orders Orders = ordersService.getById(id);
        return ResultMap.success(Orders);
    }
}
