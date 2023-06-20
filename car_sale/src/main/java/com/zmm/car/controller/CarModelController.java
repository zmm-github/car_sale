package com.zmm.car.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmm.car.vo.ResultMap;
import com.zmm.car.entity.CarModel;
import com.zmm.car.service.ICarModelService;
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
@RequestMapping("/carModel")
public class CarModelController {
    @Autowired
    private ICarModelService carModelService;

    /* 查询车型列表 */
    @GetMapping("/getCarModelList")
    public ResultMap<Map<String, Object>> getCarModelList(@RequestParam(value = "carModel", required = false) String carModel,
                                                          @RequestParam(value = "createPeople", required = false) String createPeople,
                                                          @RequestParam("currentPage") Long currentPage,
                                                          @RequestParam("pageSize") Long pageSize) {
        LambdaQueryWrapper<CarModel> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(carModel), CarModel::getCarModel, carModel);
        wrapper.like(StringUtils.hasLength(createPeople), CarModel::getCreatePeople, createPeople);

        Page<CarModel> page = new Page<>(currentPage, pageSize);
        carModelService.page(page, wrapper);

        HashMap<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        return ResultMap.success(data);
    }

    /* 新增车型 */
    @PostMapping("/addCarModel")
    public ResultMap addCarSeries(@RequestBody CarModel carModelData) {
        Date date = new Date();
        carModelData.setCreateTime(date);
        carModelService.save(carModelData);
        return ResultMap.success("新增车型成功！");
    }

    /* 删除车型 */
    @DeleteMapping("/deleteCarModelById/{id}")
    public ResultMap deleteCarModelById(@PathVariable("id") Integer id) {
        carModelService.removeById(id);
        return ResultMap.success("已成功删除编号 “" + id + "” 的车型信息！");
    }

    /* 修改车型 */
    @PutMapping("/updateCarModelById")
    public ResultMap updateCarSeriesById(@RequestBody CarModel carModelData) {
        carModelService.updateById(carModelData);
        return ResultMap.success("修改车型成功！");
    }

    /* 根据多个ID删除多个车型 */
    @DeleteMapping("/deleteCarModelByIds")
    public ResultMap deleteCarSeriesByIds(@RequestBody List<Integer> ids) {
        carModelService.removeBatchByIds(ids);
        return ResultMap.success("已成功删除编号 “" + ids + "” 的车型信息！");
    }

    /* 根据ID获取指定车型信息 */
    @GetMapping("/getCarModelById/{id}")
    public ResultMap<CarModel> getCarModelById(@PathVariable("id") Integer id) {
        CarModel carModel = carModelService.getById(id);
        return ResultMap.success(carModel);
    }

    /* 获取所有车型信息 */
    @GetMapping("/getAllCarModel")
    public ResultMap<List<CarModel>> getAllCarModel() {
        List<CarModel> carModelList = carModelService.list();
        return ResultMap.success(carModelList);
    }
}
