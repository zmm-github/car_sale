package com.zmm.car.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmm.car.vo.ResultMap;
import com.zmm.car.entity.Promotion;
import com.zmm.car.service.IPromotionService;
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
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    private IPromotionService promotionService;

    @GetMapping("/getPromotionList")
    public ResultMap<Map<String, Object>> getPromotionList(@RequestParam(value = "title", required = false) String title,
                                                          @RequestParam(value = "createPeople", required = false) String createPeople,
                                                          @RequestParam("currentPage") Long currentPage,
                                                          @RequestParam("pageSize") Long pageSize) {

        // 设置模糊查询条件
        LambdaQueryWrapper<Promotion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(title), Promotion::getTitle, title);
        queryWrapper.like(StringUtils.hasLength(createPeople), Promotion::getCreatePeople, createPeople);

        // 设置分页查询
        Page<Promotion> page = new Page<>(currentPage, pageSize);
        // 进行分页模糊查询
        promotionService.page(page, queryWrapper);

        // 向前端返回数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("rows", page.getRecords());

        // 返回
        return ResultMap.success(map);
    }

    /* 新增促销信息 */
    @PostMapping("/addPromotion")
    public ResultMap addPromotion(@RequestBody Promotion promotion) {
        Date date = new Date();
        promotion.setCreateTime(date);
        promotionService.save(promotion);
        return ResultMap.success("新增促销信息成功！");
    }

    /* 修改促销信息 */
    @PutMapping("/updatePromotion")
    public ResultMap updatePromotion(@RequestBody Promotion Promotion) {
        promotionService.updateById(Promotion);
        return ResultMap.success("修改促销信息成功！");
    }

    /* 删除促销信息 */
    @DeleteMapping("/deletePromotionById/{id}")
    public ResultMap deletePromotionById(@PathVariable("id") Integer id) {
        promotionService.removeById(id);
        return ResultMap.success("已成功删除编号 “" + id + "” 的促销信息！");
    }

    /* 删除选中的多个促销信息 */
    @DeleteMapping("/deletePromotionByIds")
    public ResultMap deletePromotionByIds(@RequestBody List<Integer> ids) {
        promotionService.removeByIds(ids);
        return ResultMap.success("已成功删除编号 “" + ids + "” 的促销信息！");
    }

    /* 根据id查询指定促销信息 */
    @GetMapping("/getPromotionById/{id}")
    public ResultMap<Promotion> getPromotionById(@PathVariable("id") Integer id) {
        Promotion promotion = promotionService.getById(id);
        return ResultMap.success(promotion);
    }
}
