package edu.friday.repository.custom.impl;

import edu.friday.repository.custom.SysRoleCustomRepository;
import edu.friday.utils.SqlUtil;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class SysRoleCustomRepositoryImpl implements SysRoleCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int batchInsertRoleUser(Long[] roleIds, Long[] userIds) {
        // 取两个数组的最小长度，避免数组越界
        int length = roleIds.length > userIds.length ? userIds.length : roleIds.length;
        StringBuffer sql = new StringBuffer();
        // 插入 sys_user_role 表（用户-角色关联表）
        sql.append("insert into sys_user_role(role_id, user_id) values ");
        // 生成批量插入的占位符（如 (?,?), (?,?)...）
        sql.append(SqlUtil.getBatchInsertSqlStr(length, 2));
        Query query = entityManager.createNativeQuery(sql.toString());

        // 设置参数
        int paramIndex = 1;
        for (int i = 0; i < length; i++) {
            query.setParameter(paramIndex++, roleIds[i]);
            query.setParameter(paramIndex++, userIds[i]);
        }
        return query.executeUpdate();
    }
}