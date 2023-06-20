package com.zmm.car.service;

import com.zmm.car.entity.RescueRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zmm.car.vo.LineVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zmm
 * @since 2023-05-25
 */
public interface IRescueRecordService extends IService<RescueRecord> {

    LineVO getRescueLine();
}
