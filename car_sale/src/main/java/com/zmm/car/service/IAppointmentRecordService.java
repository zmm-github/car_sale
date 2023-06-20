package com.zmm.car.service;

import com.zmm.car.entity.AppointmentRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zmm.car.vo.PieVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zmm
 * @since 2023-05-25
 */
public interface IAppointmentRecordService extends IService<AppointmentRecord> {

    List<PieVO> getAppointmentPie();
}
