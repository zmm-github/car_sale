package com.zmm.car.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmm.car.vo.PieVO;
import com.zmm.car.vo.ResultMap;
import com.zmm.car.entity.AppointmentRecord;
import com.zmm.car.service.IAppointmentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@RequestMapping("/appointmentRecord")
public class AppointmentRecordController {

    @Autowired
    private IAppointmentRecordService appointmentRecordService;

    @GetMapping("/getAppointmentRecordList")
    public ResultMap<Map<String, Object>> getAppointmentRecordList(@RequestParam(value = "name", required = false) String name,
                                                           @RequestParam(value = "type", required = false) String type,
                                                           @RequestParam("currentPage") Long currentPage,
                                                           @RequestParam("pageSize") Long pageSize) {

        // 设置模糊查询条件
        LambdaQueryWrapper<AppointmentRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(name), AppointmentRecord::getName, name);
        queryWrapper.like(StringUtils.hasLength(type), AppointmentRecord::getType, type);

        // 设置分页查询
        Page<AppointmentRecord> page = new Page<>(currentPage, pageSize);
        // 进行分页模糊查询
        appointmentRecordService.page(page, queryWrapper);

        // 向前端返回数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("rows", page.getRecords());

        // 返回
        return ResultMap.success(map);
    }

    /* 新增预约记录 */
    @PostMapping("/addAppointmentRecord")
    public ResultMap addAppointmentRecord(@RequestBody AppointmentRecord appointmentRecord) throws ParseException {
        Date createTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String s = format.format(createTime);
        Date arriveTime = format.parse(s);
        appointmentRecord.setArriveTime(arriveTime);
        appointmentRecord.setCreateTime(createTime);
        appointmentRecordService.save(appointmentRecord);
        return ResultMap.success("新增预约记录成功！");
    }

    /* 修改预约记录 */
    @PutMapping("/updateAppointmentRecord")
    public ResultMap updateAppointmentRecord(@RequestBody AppointmentRecord appointmentRecord) {
        appointmentRecordService.updateById(appointmentRecord);
        return ResultMap.success("修改预约记录成功！");
    }

    /* 删除预约记录 */
    @DeleteMapping("/deleteAppointmentRecordById/{id}")
    public ResultMap deleteAppointmentRecordById(@PathVariable("id") Integer id) {
        appointmentRecordService.removeById(id);
        return ResultMap.success("已成功删除编号 “" + id + "” 的预约信息！");
    }

    /* 删除选中的多个预约记录 */
    @DeleteMapping("/deleteAppointmentRecordByIds")
    public ResultMap deleteAppointmentRecordByIds(@RequestBody List<Integer> ids) {
        appointmentRecordService.removeByIds(ids);
        return ResultMap.success("已成功删除编号 “" + ids + "” 的预约信息！");
    }

    /* 根据id查询指定预约记录信息 */
    @GetMapping("/getAppointmentRecordById/{id}")
    public ResultMap<AppointmentRecord> getAppointmentRecordById(@PathVariable("id") Integer id) {
        AppointmentRecord appointmentRecord = appointmentRecordService.getById(id);
        return ResultMap.success(appointmentRecord);
    }

    /* 获取预约统计饼图 */
    @GetMapping("/getAppointmentPie")
    public ResultMap<List<PieVO>> getAppointmentPie() {
        List<PieVO> pieVOList = appointmentRecordService.getAppointmentPie();
        return ResultMap.success(pieVOList);
    }
}
