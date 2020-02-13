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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页跳转到登录及注册页面
 * @author xiaoyu
 * @date 2020/2/11 21:16
 */

@Controller
@RequestMapping("/page")
public class PageController {

    /**
     * 处理首页跳转到登录及注册页面的controller
     * @param index url参数，页数名称
     * @param redirect 订单系统的url
     * @param model model
     * @return 返回 url 的页面
     */
    @RequestMapping("/{index}")
    public String showPage(@PathVariable String index, String redirect, Model model) {
        // 从 url 接收重定向过来的订单 url，设置到域中，登录页面如果代理成功，可以从域中取订单的 url 跳转到订单页面
        model.addAttribute("redirect", redirect);
        return index;
    }

}
