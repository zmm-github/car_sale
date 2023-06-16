package com.zmm.car.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmm.car.common.vo.ResultMap;
import com.zmm.car.entity.CarSeries;
import com.zmm.car.service.ICarSeriesService;
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
@RequestMapping("/carSeries")
public class CarSeriesController {

    @Autowired
    private ICarSeriesService carSeriesService;

    /* 查询车系列表 */
    @GetMapping("/getCarSeriesList")
    public ResultMap<Map<String, Object>> searchCarSeries(@RequestParam(value = "carSeries", required = false) String carSeries,
                                                          @RequestParam(value = "createPeople", required = false) String createPeople,
                                                          @RequestParam("currentPage") Long currentPage,
                                                          @RequestParam("pageSize") Long pageSize) {
        LambdaQueryWrapper<CarSeries> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(carSeries), CarSeries::getCarSeries, carSeries);
        wrapper.like(StringUtils.hasLength(createPeople), CarSeries::getCreatePeople, createPeople);

        Page<CarSeries> page = new Page<>(currentPage, pageSize);
        carSeriesService.page(page, wrapper);

        HashMap<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        return ResultMap.success(data);
    }

    /* 新增车系 */
    @PostMapping("/addCarSeries")
    public ResultMap addCarSeries(@RequestBody CarSeries carSeriesData) {
        Date date = new Date();
        carSeriesData.setCreateTime(date);
        carSeriesService.save(carSeriesData);
        return ResultMap.success();
    }

    /* 删除车系 */
    @DeleteMapping("/deleteCarSeriesById/{id}")
    public ResultMap deleteCarSeriesById(@PathVariable("id") Integer id) {
        carSeriesService.removeById(id);
        return ResultMap.success();
    }

    /* 修改车系 */
    @PutMapping("/updateCarSeriesById")
    public ResultMap updateCarSeriesById(@RequestBody CarSeries carSeriesData) {
        carSeriesService.updateById(carSeriesData);
        return ResultMap.success();
    }

    /* 根据多个ID删除多个车系 */
    @DeleteMapping("/deleteCarSeriesByIds")
    public ResultMap deleteCarSeriesByIds(@RequestBody List<Integer> ids) {
        carSeriesService.removeBatchByIds(ids);
        return ResultMap.success();
    }

    /* 根据ID获取指定车系信息 */
    @GetMapping("/getCarSeriesById/{id}")
    public ResultMap<CarSeries> getCarSeriesById(@PathVariable("id") Integer id) {
        CarSeries carSeries = carSeriesService.getById(id);
        return ResultMap.success(carSeries);
    }

    /* 获取所有车系信息 */
    @GetMapping("/getAllCarSeries")
    public ResultMap<List<CarSeries>> getAllCarSeries() {
        List<CarSeries> carSeriesList = carSeriesService.list();
        return ResultMap.success(carSeriesList);
    }
}
