package edu.friday.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo implements Serializable {
    /** 路由名字 */
    private String name;

    /** 路由地址 */
    private String path;

    /** 是否隐藏路由，当设置 true 的时候该路由不会在侧边栏出现 */
    private String hidden;

    /** 重定向地址，当设置 noRedirect 时，该路由在面包屑导航中不可被单击 */
    private String redirect;

    /** 组件地址 */
    private String component;

    /** 当该路由下的子路由大于 1 个时，变为嵌套的模式 */
    private Boolean alwaysShow;

    /** 其他元素 */
    private MetaVo meta;

    /** 子路由 */
    private List<RouterVo> children;
}

// 注：需确保存在 MetaVo 类，其结构需根据业务定义（如包含标题、图标等路由元信息）
