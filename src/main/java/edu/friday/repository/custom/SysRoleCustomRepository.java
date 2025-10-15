package edu.friday.repository.custom;

public interface SysRoleCustomRepository {
    int batchInsertRoleUser(Long[] roleIds, Long[] userIds);

}