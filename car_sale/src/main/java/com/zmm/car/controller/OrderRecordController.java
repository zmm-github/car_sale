package com.zmm.car.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmm.car.common.vo.ResultMap;
import com.zmm.car.entity.OrderRecord;
import com.zmm.car.service.IOrderRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
@RequestMapping("/orderRecord")
public class OrderRecordController {

    @Autowired
    private IOrderRecordService orderRecordService;

    @GetMapping("/getOrderRecordList")
    public ResultMap<Map<String, Object>> getOrderRecordList(@RequestParam(value = "name", required = false) String name,
                                                           @RequestParam(value = "type", required = false) String type,
                                                           @RequestParam("currentPage") Long currentPage,
                                                           @RequestParam("pageSize") Long pageSize) {

        // 设置模糊查询条件
        LambdaQueryWrapper<OrderRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(name), OrderRecord::getName, name);
        queryWrapper.like(StringUtils.hasLength(type), OrderRecord::getType, type);

        // 设置分页查询
        Page<OrderRecord> page = new Page<>(currentPage, pageSize);
        // 进行分页模糊查询
        orderRecordService.page(page, queryWrapper);

        // 向前端返回数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("rows", page.getRecords());

        // 返回
        return ResultMap.success(map);
    }

    /* 新增预约记录 */
    @PostMapping("/addOrderRecord")
    public ResultMap addOrderRecord(@RequestBody OrderRecord orderRecord) throws ParseException {
        Date createTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String s = format.format(createTime);
        Date arriveTime = format.parse(s);
        orderRecord.setArriveTime(arriveTime);
        orderRecord.setCreateTime(createTime);
        orderRecordService.save(orderRecord);
        return ResultMap.success("新增预约记录成功！！！");
    }

    /* 修改预约记录 */
    @PutMapping("/updateOrderRecord")
    public ResultMap updateOrderRecord(@RequestBody OrderRecord orderRecord) {
        orderRecordService.updateById(orderRecord);
        return ResultMap.success("修改预约记录成功！！！");
    }

    /* 删除预约记录 */
    @DeleteMapping("/deleteOrderRecordById/{id}")
    public ResultMap deleteOrderRecordById(@PathVariable("id") Integer id) {
        orderRecordService.removeById(id);
        return ResultMap.success("删除预约记录成功！！！");
    }

    /* 删除选中的多个预约记录 */
    @DeleteMapping("/deleteOrderRecordByIds")
    public ResultMap deleteOrderRecordByIds(@RequestBody List<Integer> ids) {
        orderRecordService.removeByIds(ids);
        return ResultMap.success("已成功删除编号 “" + ids + "” 的预约记录！！！");
    }

    /* 根据id查询指定预约记录信息 */
    @GetMapping("/getOrderRecordById/{id}")
    public ResultMap<OrderRecord> getOrderRecordById(@PathVariable("id") Integer id) {
        OrderRecord orderRecord = orderRecordService.getById(id);
        return ResultMap.success(orderRecord);
    }
}
