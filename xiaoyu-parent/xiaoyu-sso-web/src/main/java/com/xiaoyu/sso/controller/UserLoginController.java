package com.xiaoyu.sso.controller;

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
import com.xiaoyu.common.utils.JsonUtils;
import com.xiaoyu.sso.service.IUserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录验证
 * @author xiaoyu
 * @date 2020/2/11 16:41
 */

@Controller
@RequestMapping("/user")
public class UserLoginController {

    /**
     * 登录的接口
     */
    @Autowired
    IUserLoginService userLoginService;

    /**
     * cookie 中 token 的 key
     */
    @Value("${XY_TOKEN_KEY}")
    private String XY_TOKEN_KEY;

    /**
     * 根据用户名和密码登录
     * @param username 用户名
     * @param password 密码
     * @return 返回登录的结果
     *         成功 status:200   data:token(redis的id)
     *         失败 status:400   data:null
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public XiaoyuResult login(HttpServletRequest request, HttpServletResponse response, String username, String password) {
        // 引入服务
        // 注入服务
        // 调用服务
        XiaoyuResult result = userLoginService.login(username, password);
        // 需要设置 token 到 cookie 中， 使用工具类  cookie需要跨域
        if (result.getStatus() == 200) {
            CookieUtils.setCookie(request, response, XY_TOKEN_KEY, result.getData().toString());
        }
        return result;
    }

    /**
     * 根据令牌 token 来获取用户信息
     * @param token 令牌
     * @return XiaoyuResult的 data 属性包含用户信息
     */
    @RequestMapping(value = "/token/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token, String callback) {

        // 调用服务
        XiaoyuResult result = userLoginService.getUserByToken(token);

        // 判斷是否jsonp请求（参数callback不为空就是jsonp请求）
        if (StringUtils.isNotBlank(callback)) {
            // 如果是jsonp请求，需要拼接成：fun({"id":1});
            String jsonpstr = callback + "(" + JsonUtils.objectToJson(result) + ");";
            return jsonpstr;
        }

        return JsonUtils.objectToJson(result);
    }

}