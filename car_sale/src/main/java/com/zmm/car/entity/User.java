package com.zmm.car.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zmm
 * @since 2023-06-03
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 性别：1表示女，0表示男
     */
    private String sex;

    /**
     * 状态
     */
    private String status;

    /**
     * 头像
     */
    private String avatar;

    @TableField(exist = false)
    private List<Integer> roleIdList;
}
