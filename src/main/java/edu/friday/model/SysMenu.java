package edu.friday.model;

import edu.friday.common.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 菜单信息实体类
 * 对应数据库表：sys_menu（菜单信息表）
 */
@Entity
@Table(name = "sys_menu", schema = "friday")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu extends BaseModel {
    /**
     * 菜单ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;

    /**
     * 菜单名称
     */
    @Basic
    @Column(name = "menu_name")
    private String menuName;

    /**
     * 父菜单ID
     */
    @Basic
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 显示顺序
     */
    @Basic
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @Basic
    @Column(name = "path")
    private String path;

    /**
     * 组件路径
     */
    @Basic
    @Column(name = "component")
    private String component;

    /**
     * 是否为外链（0是 1否）
     */
    @Basic
    @Column(name = "is_frame")
    private Integer isFrame;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @Basic
    @Column(name = "menu_type")
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    @Basic
    @Column(name = "visible")
    private String visible;

    /**
     * 权限标识
     */
    @Basic
    @Column(name = "perms")
    private String perms;

    /**
     * 菜单图标
     */
    @Basic
    @Column(name = "icon")
    private String icon;

    /**
     * 创建者（用户名）
     */
    @Basic
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;

    /**
     * 更新者（用户名）
     */
    @Basic
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @Basic
    @Column(name = "update_time")
    private Timestamp updateTime;

    /**
     * 备注信息
     */
    @Basic
    @Column(name = "remark")
    private String remark;
}
