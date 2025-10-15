package edu.friday.model;

import edu.friday.common.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 角色信息实体类
 * 对应数据库表：sys_role（角色信息表）
 */
@Entity
@Table(name = "sys_role", schema = "friday")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRole extends BaseModel {
    /**
     * 角色ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 角色名称
     */
    @Basic
    @Column(name = "role_name")
    private String roleName;

    /**
     * 角色权限字符串（用于权限控制）
     */
    @Basic
    @Column(name = "role_key")
    private String roleKey;

    /**
     * 显示顺序（用于前端展示排序）
     */
    @Basic
    @Column(name = "role_sort")
    private Integer roleSort;

    /**
     * 数据范围
     * 1：全部数据权限
     * 2：自定数据权限
     * 3：本部门数据权限
     * 4：本部门及以下数据权限
     */
    @Basic
    @Column(name = "data_scope")
    private String dataScope;

    /**
     * 角色状态
     * 0：正常
     * 1：停用
     */
    @Basic
    @Column(name = "status")
    private String status;

    /**
     * 删除标志
     * 0：代表存在
     * 2：代表删除（逻辑删除）
     */
    @Basic
    @Column(name = "del_flag")
    private String delFlag;

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