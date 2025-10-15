package edu.friday.service.impl;

import edu.friday.common.result.TableDataInfo;
import edu.friday.model.SysRole;
import edu.friday.model.vo.SysRoleVO;
import edu.friday.repository.SysRoleRepository;
import edu.friday.service.SysRoleService;
import edu.friday.utils.BeanUtils;
import edu.friday.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色表业务层实现
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Override
    public TableDataInfo selectRoleList(SysRoleVO roleVO, Pageable pageable) {
        // 将VO转换为实体类
        SysRole sysRole = new SysRole();
        BeanUtils.copyPropertiesIgnoreEmpty(roleVO, sysRole);
        // 过滤已删除的角色
        sysRole.setDelFlag("0");

        // 构建查询匹配器（支持模糊查询）
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("roleName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("roleKey", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("remark", ExampleMatcher.GenericPropertyMatchers.contains());

        // 执行分页查询
        Example<SysRole> example = Example.of(sysRole, exampleMatcher);
        Page<SysRole> rolePage = sysRoleRepository.findAll(example, pageable);

        // 转换为VO列表并返回分页结果
        List<SysRoleVO> roleVOList = rolePage.getContent().stream()
                .map(role -> {
                    SysRoleVO vo = new SysRoleVO();
                    BeanUtils.copyProperties(role, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return TableDataInfo.success(roleVOList, rolePage.getTotalElements());
    }

    @Override
    public SysRoleVO selectRoleById(Long roleId) {
        // 根据ID查询角色实体
        SysRole sysRole = sysRoleRepository.findById(roleId).orElse(null);
        if (sysRole == null) {
            return null;
        }
        // 转换为VO并返回
        SysRoleVO roleVO = new SysRoleVO();
        BeanUtils.copyProperties(sysRole, roleVO);
        return roleVO;
    }


    @Transactional
    @Override
    public boolean insertRole(SysRoleVO roleVO) {


        // 转换为实体类并保存
        SysRole sysRole = new SysRole();
        BeanUtils.copyPropertiesIgnoreEmpty(roleVO, sysRole);
        // 设置默认值（未删除）
        sysRole.setDelFlag("0");
        SysRole savedRole = sysRoleRepository.save(sysRole);

        return savedRole != null && savedRole.getRoleId() != null;
    }

    @Transactional
    @Override
    public boolean updateRole(SysRoleVO roleVO) {
        // 校验角色是否存在
        if (roleVO.getRoleId() == null || !sysRoleRepository.existsById(roleVO.getRoleId())) {
            return false;
        }
        // 转换为实体类并更新
        SysRole sysRole = new SysRole();
        BeanUtils.copyPropertiesIgnoreEmpty(roleVO, sysRole);
        SysRole updatedRole = sysRoleRepository.save(sysRole);

        return updatedRole != null;
    }

//    @Transactional
//    @Override
//    public int deleteRoleByIds(Long[] roleIds) {
//        if (roleIds == null || roleIds.length == 0) {
//            return 0;
//        }
//
//        // 逻辑删除：更新删除标志为2
//        List<Long> roleIdList = Arrays.asList(roleIds);
//        List<SysRole> roles = sysRoleRepository.findAllById(roleIdList);
//        roles.forEach(role -> role.setDelFlag("2"));
//        sysRoleRepository.saveAll(roles);
//
//        return 0;
//    }
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        return sysRoleRepository.deleteRoleByIds(roleIds);
    }


    /**
     * 查询所有角色
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return sysRoleRepository.findAll();
    }
    /**
     * 根据用户ID获取角色选择框列表
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return sysRoleRepository.selectRoleIdsByUserId(userId);
    }
    @Override
    @Transactional
    public boolean updateRoleStatus(Long roleId, String status, String updateBy) {
        // 校验角色是否存在
        SysRole sysRole = sysRoleRepository.findById(roleId).orElse(null);
        if (sysRole == null) {
            return false; // 角色不存在，修改失败
        }
        // 执行状态更新
        int rows = sysRoleRepository.updateRoleStatus(roleId, status, updateBy);
        return rows > 0; // 返回是否更新成功
    }
    /**
     * 根据用户 ID 查询权限
     * @param userId 用户 ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = sysRoleRepository.selectRoleByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }
}