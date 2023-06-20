package com.zmm.car.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmm.car.vo.ResultMap;
import com.zmm.car.entity.Accessory;
import com.zmm.car.service.IAccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/accessory")
public class AccessoryController {

    @Autowired
    private IAccessoryService accessoryService;

    @GetMapping("/getAccessoryList")
    public ResultMap<Map<String, Object>> getAccessoryList(@RequestParam(value = "accessoryName", required = false) String accessoryName,
                                                          @RequestParam(value = "carSeries", required = false) String carSeries,
                                                          @RequestParam("currentPage") Long currentPage,
                                                          @RequestParam("pageSize") Long pageSize) {

        // 设置模糊查询条件
        LambdaQueryWrapper<Accessory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(accessoryName), Accessory::getAccessoryName, accessoryName);
        queryWrapper.like(StringUtils.hasLength(carSeries), Accessory::getCarSeries, carSeries);

        // 设置分页查询
        Page<Accessory> page = new Page<>(currentPage, pageSize);
        // 进行分页模糊查询
        accessoryService.page(page, queryWrapper);

        // 向前端返回数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("rows", page.getRecords());

        // 返回
        return ResultMap.success(map);
    }

    /* 新增配件 */
    @PostMapping("/addAccessory")
    public ResultMap addAccessory(@RequestBody Accessory accessory) {
        Date date = new Date();
        accessory.setCreateTime(date);
        accessoryService.save(accessory);
        return ResultMap.success("新增配件成功！");
    }

    /* 修改配件 */
    @PutMapping("/updateAccessory")
    public ResultMap updateAccessory(@RequestBody Accessory accessory) {
        accessoryService.updateById(accessory);
        return ResultMap.success("修改配件成功！");
    }

    /* 删除配件 */
    @DeleteMapping("/deleteAccessoryById/{id}")
    public ResultMap deleteAccessoryById(@PathVariable("id") Integer id) {
        accessoryService.removeById(id);
        return ResultMap.success("已成功删除编号 “" + id + "” 的配件信息！");
    }

    /* 删除选中的多个配件 */
    @DeleteMapping("/deleteAccessoryByIds")
    public ResultMap deleteAccessoryByIds(@RequestBody List<Integer> ids) {
        accessoryService.removeByIds(ids);
        return ResultMap.success("已成功删除编号 “" + ids + "” 的配件信息！");
    }

    /* 根据id查询指定配件信息 */
    @GetMapping("/getAccessoryById/{id}")
    public ResultMap<Accessory> getAccessoryById(@PathVariable("id") Integer id) {
        Accessory accessory = accessoryService.getById(id);
        return ResultMap.success(accessory);
    }

}
