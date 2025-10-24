package edu.friday.service.impl;

import edu.friday.common.result.TreeSelect;
import edu.friday.model.SysMenu;
import edu.friday.model.vo.MetaVo;
import edu.friday.repository.SysMenuRepository;
import edu.friday.service.SysMenuService;
import edu.friday.model.vo.RouterVo;
import edu.friday.model.vo.SysMenuVO;
import edu.friday.utils.BeanUtils;
import edu.friday.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单业务层处理
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuRepository sysMenuRepository;

    /**
     * 查询系统菜单列表
     * @param menu 菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenuVO menu, Long userId) {
        List<SysMenu> menuList = null;
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(menu, sysMenu);
        // 管理员显示所有菜单信息
        if (isAdmin(userId)) {
            menuList = sysMenuRepository.findAll(Example.of(sysMenu));
        } else {
            menuList = sysMenuRepository.selectMenuListByUserId(sysMenu, userId);
        }
        return menuList;
    }

    /**
     * 根据菜单ID查询信息
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu selectMenuById(Long menuId) {
        return sysMenuRepository.findById(menuId).orElse(null);
    }

    /**
     * 新增保存菜单信息
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysMenu menu) {
        SysMenu savedMenu = sysMenuRepository.save(menu);
        return savedMenu != null && savedMenu.getMenuId() != null ? 1 : 0;
    }

    /**
     * 修改保存菜单信息
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysMenu menu) {
        return sysMenuRepository.findById(menu.getMenuId())
                .map(existingMenu -> {
                    BeanUtils.copyProperties(menu, existingMenu, "menuId");
                    sysMenuRepository.save(existingMenu);
                    return 1;
                })
                .orElse(0);
    }

    /**
     * 删除菜单管理信息
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId) {
        sysMenuRepository.deleteById(menuId);
        return 1;
    }

    /**
     * 根据用户ID查询权限
     * @param userId 用户ID
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

    @Override
    public List<SysMenu> selectMenuList(Long userId) {
        return selectMenuList(new SysMenuVO(), userId);
    }

    @Override
    public List<SysMenuVO> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = isAdmin(userId)
                ? sysMenuRepository.selectMenuTreeAll()
                : sysMenuRepository.selectMenuTreeByUserId(userId);
        List<SysMenuVO> menuVOs = BeanUtils.copyProperties(menus, SysMenuVO.class);
        return getChildPerms(menuVOs, 0);
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        List<SysMenu> menus = sysMenuRepository.selectMenuListByRoleId(roleId);
        return menus.stream().map(SysMenu::getMenuId).collect(Collectors.toList());
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(menuId);
        long count = sysMenuRepository.count(Example.of(sysMenu));
        return count > 0;
    }

    @Override
    public boolean checkMenuExistRole(Long menuId) {
        long count = sysMenuRepository.checkMenuExistRole(menuId);
        return count > 0;
    }

    @Override
    public String checkMenuNameUnique(SysMenu menu) {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(menu.getParentId());
        sysMenu.setMenuName(menu.getMenuName());
        Page<SysMenu> page = sysMenuRepository.findAll(Example.of(sysMenu), PageRequest.of(0, 1));
        List<SysMenu> list = page.getContent();
        if (!list.isEmpty()) {
            SysMenu info = list.get(0);
            if (StringUtils.isNotNull(info) && !info.getMenuId().equals(menuId)) {
                return "NOT_UNIQUE";
            }
        }
        return "UNIQUE";
    }

    @Override
    public List<RouterVo> buildMenus(List<SysMenuVO> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenuVO menu : menus) {
            RouterVo router = new RouterVo();
            router.setName(StringUtils.capitalize(menu.getPath()));
            router.setPath(getRouterPath(menu));
            router.setComponent(StringUtils.isBlank(menu.getComponent()) ? "Layout" : menu.getComponent());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
            List<SysMenuVO> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && "M".equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            routers.add(router);
        }
        return routers;
    }

    @Override
    public List<SysMenuVO> buildMenuTree(List<SysMenuVO> menus) {
        List<SysMenuVO> returnList = new ArrayList<>();
        Iterator<SysMenuVO> iterator = menus.iterator();
        while (iterator.hasNext()) {
            SysMenuVO t = iterator.next();
            if (t.getParentId() == 0) {
                recursionFn(menus, t);
                returnList.add(t);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenuVO> menus) {
        List<SysMenuVO> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysMenuVO> list, SysMenuVO t) {
        List<SysMenuVO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenuVO child : childList) {
            if (hasChild(list, child)) {
                Iterator<SysMenuVO> it = childList.iterator();
                while (it.hasNext()) {
                    SysMenuVO n = it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenuVO> getChildList(List<SysMenuVO> list, SysMenuVO t) {
        List<SysMenuVO> tlist = new ArrayList<>();
        Iterator<SysMenuVO> it = list.iterator();
        while (it.hasNext()) {
            SysMenuVO n = it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getMenuId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenuVO> list, SysMenuVO t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 根据父节点的ID获取所有子节点
     */
    private List<SysMenuVO> getChildPerms(List<SysMenuVO> list, int parentId) {
        List<SysMenuVO> returnList = new ArrayList<>();
        Iterator<SysMenuVO> iterator = list.iterator();
        while (iterator.hasNext()) {
            SysMenuVO t = iterator.next();
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 获取路由地址
     */
    public String getRouterPath(SysMenuVO menu) {
        String routerPath = menu.getPath();
        if (0 == menu.getParentId() && "1".equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        return routerPath;
    }

    /**
     * 判断是否为管理员
     */
    private boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}