package com.zmm.car.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author zmm
 * @since 2023-06-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    /**
     * 组件
     */
    private String component;

    /**
     * 路径
     */
    private String path;

    /**
     * 重定向路径
     */
    private String redirect;

    /**
     * 名称
     */
    private String name;

    /**
     * 标题
     */
    private String title;

    /**
     * 图标
     */
    private String icon;

    /**
     * 对应的父ID
     */
    private Integer parentId;

    /**
     * 是否叶子节点
     */
    private String isLeaf;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children;

    @TableField(exist = false)
    private Map<String, Object> meta;

    public Map<String, Object> getMeta() {
        meta = new HashMap<>();
        meta.put("title", title);
        meta.put("icon", icon);
        return meta;
    }

}
