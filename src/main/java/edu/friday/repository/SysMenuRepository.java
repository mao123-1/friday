package edu.friday.repository;

import edu.friday.model.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单表 数据层
 */
@Repository
public interface SysMenuRepository extends JpaRepository<SysMenu, Long> {
    String JOIN_ROLE_MENU = " left join sys_role_menu rm on m.menu_id = rm.menu_id ";
    String JOIN_USER_ROLE = " left join sys_user_role ur on rm.role_id = ur.role_id ";

    @Query(value = " select distinct m.perms from sys_menu m "
            + JOIN_ROLE_MENU + JOIN_USER_ROLE + " where ur.user_id = :userId", nativeQuery = true)
    List<String> selectMenuPermsByUserId(@Param("userId") Long userId);
}