package edu.friday.service.impl;

import edu.friday.repository.SysMenuRepository;
import edu.friday.service.SysMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单业务层处理
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    SysMenuRepository sysMenuRepository;

    /**
     * 根据用户 ID 查询权限
     * @param userId 用户 ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = sysMenuRepository.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }
}
