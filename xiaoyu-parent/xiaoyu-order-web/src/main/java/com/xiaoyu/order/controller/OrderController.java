package com.xiaoyu.order.controller;

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

import com.xiaoyu.cart.service.ICartItemService;
import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.common.utils.CookieUtils;
import com.xiaoyu.manager.pojo.TbItem;
import com.xiaoyu.manager.pojo.TbUser;
import com.xiaoyu.sso.service.IUserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单管理
 * 
 * @author xiaoyu
 * @date 2020/2/13 21:11
 */
@Controller
public class OrderController {

    /**
     * 调用SSO的服务获取用户信息
     */
    @Autowired
    private IUserLoginService userLoginService;

    /**
     * 调用购物车服务，从 redis 中获取该用户的购物车商品列表
     */
    @Autowired
    private ICartItemService cartItemService;

    /**
     * cookie中的令牌token：存储用户名的key
     */
    @Value("${XY_TOKEN_KEY}")
    private String XY_TOKEN_KEY;

    @RequestMapping("/order/order-cart")
    public String showOrder(HttpServletRequest request) {

        // 因为拦截器中以及将用户信息设置到request中，不用再次查询用户信息。

//        // 从cookie中获取 令牌 token：存储用户名的key
//        String token = CookieUtils.getCookieValue(request, XY_TOKEN_KEY);
//        // 必须是登录待显示订单页面
//        if (StringUtils.isNotBlank(token)) {
//            // 调用SSO的服务获取用户信息
//            XiaoyuResult result = userLoginService.getUserByToken(token);
//            if (result.getStatus() == 200) {
//                // 必须是登录待显示订单页面
//                TbUser user = (TbUser) result.getData();
//                // 展示用户的送货地址：根据用户的 id 查询该用户的配送地址。（这里静态资源）
//                // 调用购物车服务，从 redis 中获取该用户的购物车商品列表
//                List<TbItem> cartList = cartItemService.queryCartListByUserId(user.getId());
//                // 将列表展示到页面中：通过model传递
//                request.setAttribute("cartList", cartList);
//            }
//        }

        TbUser user = (TbUser) request.getAttribute("USER_INFO");
        // 展示用户的送货地址：根据用户的 id 查询该用户的配送地址。（这里静态资源）
        // 调用购物车服务，从 redis 中获取该用户的购物车商品列表
        List<TbItem> cartList = cartItemService.queryCartListByUserId(user.getId());
        // 将列表展示到页面中：通过model传递
        request.setAttribute("cartList", cartList);
        return "order-cart";
    }

}
