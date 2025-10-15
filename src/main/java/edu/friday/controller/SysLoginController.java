package edu.friday.controller;

import edu.friday.common.constant.Constants;
import edu.friday.common.result.RestResult;
import edu.friday.common.security.service.LoginUser;
import edu.friday.common.security.service.SysLoginService;
import edu.friday.common.security.service.SysPermissionService;
import edu.friday.common.security.service.TokenService;
import edu.friday.model.vo.SysUserVO;
import edu.friday.utils.http.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


/**
 * 登录验证
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录方法
     * @param username 用户名
     * @param password 密码
     * @param uuid
     * @return 结果
     */
    @PostMapping({"/login", "/"})
    public RestResult login(String username, String password, String uuid) {
        RestResult ajax = RestResult.success();
        // 生成令牌
        String token = loginService.login(username, password, "123", uuid);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }
    /**
     * 获取登录用户授权信息
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public RestResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUserVO user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        RestResult ajax = RestResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }
}
