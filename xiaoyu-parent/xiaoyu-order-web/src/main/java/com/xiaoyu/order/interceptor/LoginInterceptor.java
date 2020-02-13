package com.xiaoyu.order.interceptor;

/**
 *                     .::::.
 *                   .::::::::.
 *                  :::::::::::    佛主保佑、永无Bug
 *              ..:::::::::::'
 *            '::::::::::::'
 *              .::::::::::
 *         '::::::::::::::..
 *              ..::::::::::::.
 *            ``::::::::::::::::
 *             ::::``:::::::::'        .:::.
 *            ::::'   ':::::'       .::::::::.
 *          .::::'      ::::     .:::::::'::::.
 *         .:::'       :::::  .:::::::::' ':::::.
 *        .::'        :::::.:::::::::'      ':::::.
 *       .::'         ::::::::::::::'         ``::::.
 *   ...:::           ::::::::::::'              ``::.
 *  ```` ':.          ':::::::::'                  ::::..
 *                     '.:::::'                    ':'````..
 */

import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.common.utils.CookieUtils;
import com.xiaoyu.sso.service.IUserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户身份认证的拦截器
 * 
 * @author xiaoyu
 * @date 2020/2/13 22:34
 */
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 调用SSO的服务获取用户信息
     */
    @Autowired
    private IUserLoginService userLoginService;

    /**
     * cookie中的令牌token：存储用户名的key
     */
    @Value("${XY_TOKEN_KEY}")
    private String XY_TOKEN_KEY;

    /**
     * SSO 服务的url
     */
    @Value("${SSO_URL}")
    private String SSO_URL;

    /**
     * 在进入目标方法之前执行
     * 可以身份认证等
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        // 身份认证
        // 从cookie中获取 令牌 token：存储用户名的key
        String token = CookieUtils.getCookieValue(httpServletRequest, XY_TOKEN_KEY);
        // 判断 token 是否存在
        if (StringUtils.isEmpty(token)) {
            // token 不存在，说明没登录 --》重定向到登录页面
            // httpServletRequest.getRequestURL()：访问的url  localhost:8092/order/order-cart.html
            httpServletResponse.sendRedirect(SSO_URL + "/page/login?redirect=" + httpServletRequest.getRequestURL());
            return false;
        }
        // 如果 token 存在，调用 SSO 服务查看用户信息 --》看用户是否已经过期
        XiaoyuResult result = userLoginService.getUserByToken(token);
        if (result.getStatus() != 200) {
            // 用户已经过期 --》重定向到登录页面
            httpServletResponse.sendRedirect(SSO_URL + "/page/login?redirect=" + httpServletRequest.getRequestURL());
            return false;
        }
        // 用户没过期，说明登录了 --》放行
        // 设置用户信息到request中，目标方法可以从request中获取用户信息，不用写重复代码
        httpServletRequest.setAttribute("USER_INFO", result.getData());
        return true;
    }

    /**
     * 在进入目标方法之后，返回 ModelAndView 之前执行
     * 用于设置一些公共变量
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 返回 ModelAndView 之后，渲染到页面之前
     * 处理异常，日志清理等
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
