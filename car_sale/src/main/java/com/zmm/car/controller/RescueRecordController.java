package com.zmm.car.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmm.car.vo.LineVO;
import com.zmm.car.vo.ResultMap;
import com.zmm.car.entity.RescueRecord;
import com.zmm.car.service.IRescueRecordService;
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
 * 前端控制器
 * </p>
 *
 * @author zmm
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/rescueRecord")
public class RescueRecordController {

    @Autowired
    private IRescueRecordService rescueRecordService;

    @GetMapping("/getRescueRecordList")
    public ResultMap<Map<String, Object>> getRescueRecordList(@RequestParam(value = "name", required = false) String name,
                                                              @RequestParam(value = "content", required = false) String content,
                                                              @RequestParam("currentPage") Long currentPage,
                                                              @RequestParam("pageSize") Long pageSize) {

        // 设置模糊查询条件
        LambdaQueryWrapper<RescueRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(name), RescueRecord::getName, name);
        queryWrapper.like(StringUtils.hasLength(content), RescueRecord::getContent, content);

        // 设置分页查询
        Page<RescueRecord> page = new Page<>(currentPage, pageSize);
        // 进行分页模糊查询
        rescueRecordService.page(page, queryWrapper);

        // 向前端返回数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("rows", page.getRecords());

        // 返回
        return ResultMap.success(map);
    }

    /* 新增预约记录 */
    @PostMapping("/addRescueRecord")
    public ResultMap addRescueRecord(@RequestBody RescueRecord rescueRecord) throws ParseException {
        Date createTime = new Date();
        rescueRecord.setCreateTime(createTime);
        rescueRecordService.save(rescueRecord);
        return ResultMap.success("新增救援记录成功！");
    }

    /* 修改预约记录 */
    @PutMapping("/updateRescueRecord")
    public ResultMap updateRescueRecord(@RequestBody RescueRecord rescueRecord) {
        rescueRecordService.updateById(rescueRecord);
        return ResultMap.success("修改救援记录成功！");
    }

    /* 删除预约记录 */
    @DeleteMapping("/deleteRescueRecordById/{id}")
    public ResultMap deleteRescueRecordById(@PathVariable("id") Integer id) {
        rescueRecordService.removeById(id);
        return ResultMap.success("已成功删除编号 “" + id + "” 的救援信息！");
    }

    /* 删除选中的多个预约记录 */
    @DeleteMapping("/deleteRescueRecordByIds")
    public ResultMap deleteRescueRecordByIds(@RequestBody List<Integer> ids) {
        rescueRecordService.removeByIds(ids);
        return ResultMap.success("已成功删除编号 “" + ids + "” 的救援信息！");
    }

    /* 根据id查询指定预约记录信息 */
    @GetMapping("/getRescueRecordById/{id}")
    public ResultMap<RescueRecord> getRescueRecordById(@PathVariable("id") Integer id) {
        RescueRecord rescueRecord = rescueRecordService.getById(id);
        return ResultMap.success(rescueRecord);
    }

    /* 获取救援统计折线图 */
    @GetMapping("/getRescueLine")
    public ResultMap<LineVO> getRescueLine() {
       LineVO lineVO = rescueRecordService.getRescueLine();
       return ResultMap.success(lineVO);
    }
}
