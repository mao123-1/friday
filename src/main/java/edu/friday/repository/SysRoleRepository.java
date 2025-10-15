package edu.friday.repository;

import edu.friday.model.SysRole;
import edu.friday.repository.custom.SysRoleCustomRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long>, SysRoleCustomRepository {

    // 根据角色名称查询角色
    SysRole findByRoleName(String roleName);

    // 根据角色权限字符串查询角色
    SysRole findByRoleKey(String roleKey);

    // 根据角色状态查询角色列表
    List<SysRole> findByStatus(String status);

    // 根据删除标志查询角色列表（排除已删除的角色）
    List<SysRole> findByDelFlag(String delFlag);
    /**
     * 批量删除角色（逻辑删除）
     * @param roleIds 角色ID数组
     * @return 影响的行数
     */
    @Modifying
    @Query(value = "update sys_role set del_flag = '2' where role_id in :roleIds", nativeQuery = true)
    int deleteRoleByIds(@Param("roleIds") Long[] roleIds);
    final String JOIN_USER_ROLE = " left join sys_user_role ur on ur.role_id = r.role_id ";
    final String JOIN_USER = "left join sys_user u on u.user_id = ur.user_id ";

    @Query(value = " select r.role_id from sys_role r " + JOIN_USER_ROLE + JOIN_USER +
            "WHERE r.del_flag = '0' and u.user_id = :userId ", nativeQuery = true)
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
    int batchInsertRoleUser(Long[] roleIds, Long[] userIds);
    @Modifying
    @Query(value = " delete from sys_role_menu where role_id=:roleId ", nativeQuery = true)
    int deleteRoleMenuByRoleId(@Param("roleId")Long roleId);
    /**
     * 更新角色状态
     * @param roleId 角色ID
     * @param status 目标状态（如"0"启用、"1"禁用）
     * @param updateBy 更新人
     * @return 影响行数
     */
    @Modifying
    @Query(value = "UPDATE sys_role SET status = :status, update_by = :updateBy, update_time = NOW() WHERE role_id = :roleId", nativeQuery = true)
    int updateRoleStatus(@Param("roleId") Long roleId, @Param("status") String status, @Param("updateBy") String updateBy);
    @Query(value = "select r.* from sys_role r " + JOIN_USER_ROLE + JOIN_USER +
            "WHERE r.del_flag = '0' and u.user_id = :userId ", nativeQuery = true)
    List<SysRole> selectRoleByUserId(@Param("userId") Long userId);
}