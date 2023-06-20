package com.zmm.car.service.impl;

import com.zmm.car.entity.RescueRecord;
import com.zmm.car.mapper.RescueRecordMapper;
import com.zmm.car.service.IRescueRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmm.car.utils.ColorUtil;
import com.zmm.car.vo.LineVO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zmm
 * @since 2023-05-25
 */
@Service
public class RescueRecordServiceImpl extends ServiceImpl<RescueRecordMapper, RescueRecord> implements IRescueRecordService {

    @Override
    public LineVO getRescueLine() {
        // 1、查询所有救援记录
        List<RescueRecord> rescueList = this.baseMapper.selectList(null);
        /*
        * TreeMap: 能够把它保存的记录根据key排序,默认是按升序排序，也可以指定排序的比较器，
        * 当用Iterator 遍历TreeMap时，得到的记录是排过序的。TreeMap不允许key的值为null。非同步的。
        * */
        TreeMap<String, Integer> amountMap = new TreeMap<>();
        // 2、遍历统计每个月救援的次数
        for (RescueRecord record : rescueList) {
                Date date = record.getCreateTime();
                int year = date.getYear() + 1900;
                int month = date.getMonth() + 1;
            String createTime = year + "-" + month;
            if (amountMap.containsKey(createTime)) {
                amountMap.put(createTime, amountMap.get(createTime) + 1);
            } else {
                amountMap.put(createTime, 1);
            }
        }

        // 3、创建线型图对象
        LineVO lineVO = new LineVO();
        // 4、创建列表
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        // 5、循环遍历amountMap进行赋值
        for (Map.Entry<String, Integer> entry:amountMap.entrySet()){
            dateList.add(entry.getKey());
            values.add(entry.getValue());
        }
        lineVO.setDateList(dateList);
        lineVO.setValues(values);
        // 8、返回结果
        return lineVO;
    }
}
