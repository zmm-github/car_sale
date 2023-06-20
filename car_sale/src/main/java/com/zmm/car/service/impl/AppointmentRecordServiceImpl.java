package com.zmm.car.service.impl;

import com.zmm.car.entity.AppointmentRecord;
import com.zmm.car.mapper.AppointmentRecordMapper;
import com.zmm.car.service.IAppointmentRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmm.car.utils.ColorUtil;
import com.zmm.car.vo.PieVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zmm
 * @since 2023-05-25
 */
@Service
public class AppointmentRecordServiceImpl extends ServiceImpl<AppointmentRecordMapper, AppointmentRecord> implements IAppointmentRecordService {

    @Override
    public List<PieVO> getAppointmentPie() {
        // 1、查询所有预约记录
        List<AppointmentRecord> appointmentList = this.baseMapper.selectList(null);
        HashMap<String, Integer> amountMap = new HashMap<>();
        // 2、遍历统计每个车系预约的数量
        for (AppointmentRecord appointment : appointmentList) {
            if (amountMap.containsKey(appointment.getCarSeries())) {
                amountMap.put(appointment.getCarSeries(), amountMap.get(appointment.getCarSeries()) + 1);
            } else {
                amountMap.put(appointment.getCarSeries(), 1);
            }
        }

        // 3、创建饼图列表
        ArrayList<PieVO> pieVOList = new ArrayList<>();
        // 4、循环遍历预约记录列表进行赋值
        for (AppointmentRecord appointment : appointmentList) {
            // 5、创建饼图实体对象
            PieVO pieVO = new PieVO();
            // 6、为饼图实体赋值
            pieVO.setName(appointment.getCarSeries());
            pieVO.setValue(amountMap.get(appointment.getCarSeries()));
            pieVO.setItemStyle(ColorUtil.createItemStyle(amountMap.get(appointment.getCarSeries())));
            if (!pieVOList.contains(pieVO)) {
                // 7、加入到饼图列表中
                pieVOList.add(pieVO);
            }
        }

        // 8、返回结果
        return pieVOList;
    }
}
