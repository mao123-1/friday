package edu.friday.controller;

import edu.friday.common.base.BaseController;
import edu.friday.common.result.RestResult;
import edu.friday.common.result.TableDataInfo;
import edu.friday.model.vo.SysRoleVO;
import edu.friday.service.SysRoleService;
import edu.friday.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    @Autowired
    SysRoleService roleService;
    @GetMapping("/list")
    public TableDataInfo list(SysRoleVO role, Pageable page) {
        int pageNum = page.getPageNumber() - 1;
        pageNum = pageNum <= 0 ? 0 : pageNum;
        page = PageRequest.of(pageNum, page.getPageSize());
        return roleService.selectRoleList(role, page);
    }
    @PostMapping
    public RestResult add(@RequestBody SysRoleVO role) {
        role.setCreateBy("system");
        boolean flag = roleService.insertRole(role);
        return toAjax(flag ? 1 : 0);
    }
    @DeleteMapping("/{roleIds}")
    public RestResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }
    /**
     * 修改角色
     */
    @PutMapping
    public RestResult edit(@Validated @RequestBody SysRoleVO role) {
        role.setUpdateBy("system");
        boolean flag = roleService.updateRole(role);
        return toAjax(flag ? 1 : 0);
    }
    /**
     * 根据角色编号获取角色详细信息（包含关联逻辑，若有需要可扩展）
     */
    @GetMapping(value = {"", "/{roleId}"})
    public RestResult getInfo(@PathVariable(value = "roleId", required = false) Long roleId) {
        RestResult ajax = RestResult.success();
        // 1. 获取所有角色列表（类似原代码中获取所有角色的逻辑）
        ajax.put("roles", roleService.selectRoleAll());

        // 2. 根据角色编号获取角色详情（若传入roleId）
        if (StringUtils.isNotNull(roleId)) {
            ajax.put(RestResult.DATA_TAG, roleService.selectRoleById(roleId));

            // 若需要关联“用户-角色”映射（可选，根据业务需求）
            // ajax.put("userIds", userService.selectUserIdListByRoleId(roleId)); // 假设查询拥有该角色的用户ID列表
        }
        return ajax;
    }
    /**
     * 修改角色状态接口（使用SysRoleVO接收请求体参数）
     */
    @PutMapping("/changeStatus")
    public RestResult updateStatus(@RequestBody SysRoleVO roleVO) {
        // 1. 校验参数是否存在
        if (roleVO.getRoleId() == null) {
            return RestResult.error("参数缺失：roleId（角色ID）不能为空");
        }
        if (roleVO.getStatus() == null || (!"0".equals(roleVO.getStatus()) && !"1".equals(roleVO.getStatus()))) {
            return RestResult.error("参数错误：status（角色状态）需为 0（禁用）或 1（启用）");
        }

        // 2. 调用服务层修改状态
        boolean success = roleService.updateRoleStatus(roleVO.getRoleId(), roleVO.getStatus(), "system");
        return success ? RestResult.success("角色状态修改成功") : RestResult.error("角色状态修改失败");
    }
}
