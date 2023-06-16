package com.zmm.car.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zmm
 * @since 2023-06-13
 */
@Data
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客户身份证
     */
    private String card;

    /**
     * 客户姓名
     */
    private String name;

    /**
     * 客户手机号
     */
    private String phoneNumber;

    /**
     * 售价
     */
    private BigDecimal price;

    /**
     * 销售员姓名
     */
    private String saleName;

    /**
     * 支付状态
     */
    private String status;

    /**
     * 所属车型
     */
    private String carModel;

    /**
     * 所属车系
     */
    private String carSeries;

    /**
     * 颜色
     */
    private String color;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
