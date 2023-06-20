package com.zmm.car.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmm.car.vo.ResultMap;
import com.zmm.car.entity.Customer;
import com.zmm.car.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zmm
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping("/getCustomerList")
    public ResultMap<Map<String, Object>> getCustomerList(@RequestParam(value = "name", required = false) String name,
                                                          @RequestParam(value = "status", required = false) String status,
                                                          @RequestParam("currentPage") Long currentPage,
                                                          @RequestParam("pageSize") Long pageSize) {

        // 设置模糊查询条件
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(name), Customer::getName, name);
        queryWrapper.like(StringUtils.hasLength(status), Customer::getStatus, status);

        // 设置分页查询
        Page<Customer> page = new Page<>(currentPage, pageSize);
        // 进行分页模糊查询
        customerService.page(page, queryWrapper);

        // 向前端返回数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("rows", page.getRecords());

        // 返回
        return ResultMap.success(map);
    }

    /* 新增顾客 */
    @PostMapping("/addCustomer")
    public ResultMap addCustomer(@RequestBody Customer customer) {
        Date date = new Date();
        customer.setApplicationTime(date);
        customerService.save(customer);
        return ResultMap.success("新增顾客成功！");
    }

    /* 修改顾客 */
    @PutMapping("/updateCustomer")
    public ResultMap updateCustomer(@RequestBody Customer customer) {
        customerService.updateById(customer);
        return ResultMap.success("修改顾客成功！");
    }

    /* 删除顾客 */
    @DeleteMapping("/deleteCustomerById/{id}")
    public ResultMap deleteCustomerById(@PathVariable("id") Integer id) {
        customerService.removeById(id);
        return ResultMap.success("已成功删除编号 “" + id + "” 的顾客信息！");
    }

    /* 删除选中的多个顾客 */
    @DeleteMapping("/deleteCustomerByIds")
    public ResultMap deleteCustomerByIds(@RequestBody List<Integer> ids) {
        customerService.removeByIds(ids);
        return ResultMap.success("已成功删除编号 “" + ids + "” 的顾客信息！");
    }

    /* 根据id查询指定顾客信息 */
    @GetMapping("/getCustomerById/{id}")
    public ResultMap<Customer> getCustomerById(@PathVariable("id") Integer id) {
        Customer customer = customerService.getById(id);
        return ResultMap.success(customer);
    }
}
