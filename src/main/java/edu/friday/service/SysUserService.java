package edu.friday.service;

import edu.friday.common.result.TableDataInfo;
import edu.friday.model.SysUser;
import edu.friday.model.vo.SysUserVO;
import org.springframework.data.domain.Pageable;
// 移除 import java.awt.print.Pageable; 这行错误的导入

/**
 * 用户表业务层接口
 */
public interface SysUserService {
    public TableDataInfo selectUserList(SysUserVO user, Pageable page);
    String checkUserNameUnique(String userName);
    /**
     * 校验用户手机是否唯一
     * @param userInfo 用户信息
     * @return
     */
    String checkPhoneUnique(SysUserVO userInfo);
    /**
     * 校验Email是否唯一
     * @param userInfo 用户信息
     * @return
     */
    String checkEmailUnique(SysUserVO userInfo);
    /**
     * 新增并保存用户信息
     * @param user 用户信息
     * @return 结果
     */
    boolean insertUser(SysUserVO user);
    /**
     * 批量删除用户信息
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    int deleteUserByIds(Long[] userIds);
    /**
     * 修改并保存用户信息
     * @param user 用户信息
     * @return 结果
     */
    boolean updateUser(SysUserVO user);
    /**
     * 通过用户 ID 查询用户
     * @param userId 用户 ID
     * @return 用户对象信息
     */
    SysUser selectUserById(Long userId);
    SysUser selectUserByUserName(String userName);
}