package edu.friday.service;

import edu.friday.common.result.TableDataInfo;
import edu.friday.model.SysRole;
import edu.friday.model.vo.SysRoleVO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * 角色表业务层接口
 */
public interface SysRoleService {

    /**
     * 查询角色列表
     * @param role 角色查询条件
     * @param page 分页参数
     * @return 分页角色数据
     */
    TableDataInfo selectRoleList(SysRoleVO role, Pageable page);

    /**
     * 根据角色ID查询角色信息
     * @param roleId 角色ID
     * @return 角色信息
     */
    SysRoleVO selectRoleById(Long roleId);

    /**
     * 新增角色
     * @param role 角色信息
     * @return 新增结果（true/false）
     */
    boolean insertRole(SysRoleVO role);

    /**
     * 修改角色
     * @param role 角色信息
     * @return 修改结果（true/false）
     */
    boolean updateRole(SysRoleVO role);


    /**
     * 批量删除角色信息
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    int deleteRoleByIds(Long[] roleIds);

    List<SysRole> selectRoleAll();
    List<Long> selectRoleListByUserId(Long userId);
    /**
     * 修改角色状态
     * @param roleId 角色ID
     * @param status 目标状态
     * @param updateBy 更新人
     * @return 是否成功（true-成功，false-失败）
     */
    boolean updateRoleStatus(Long roleId, String status, String updateBy);
    Set<String> selectRolePermissionByUserId(Long userId);
}