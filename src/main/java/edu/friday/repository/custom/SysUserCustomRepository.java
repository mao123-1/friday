package edu.friday.repository.custom;
import org.springframework.stereotype.Repository;
/**
 * 用户表数据层
 */
@Repository
public interface SysUserCustomRepository {
    int batchInsertUserRole(Long[] userIds, Long[] roles);
}