package edu.friday.service;

import java.util.Set;

/**
 * 菜单业务层
 */
public interface SysMenuService {
    /**
     * 根据用户 ID 查询权限
     * @param userId 用户 ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByUserId(Long userId);
}
