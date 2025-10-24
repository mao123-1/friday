package edu.friday.common.security.handle;

import com.alibaba.fastjson.JSON;
import edu.friday.common.result.RestResult;
import edu.friday.utils.StringUtils;
import edu.friday.utils.http.ServletUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException {
        int code = HttpStatus.UNAUTHORIZED.value();
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(RestResult.error(code, msg)));
    }
}

// 注：上述代码中涉及的 ServletUtils、RestResult 需根据实际项目中的工具类和结果封装类进行调整，
// 若项目中无这些自定义类，需自行实现或替换为对应的响应处理逻辑。
