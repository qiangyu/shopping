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
import com.xiaoyu.manager.pojo.TbItem;
import com.xiaoyu.manager.pojo.TbUser;
import com.xiaoyu.order.pojo.OrderInfo;
import com.xiaoyu.order.service.IOrderService;
import com.xiaoyu.sso.service.IUserLoginService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单管理
 * 
 * @author xiaoyu
 * @date 2020/2/13 21:11
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    /**
     * 调用购物车服务，从 redis 中获取该用户的购物车商品列表
     */
    @Autowired
    private ICartItemService cartItemService;

    /**
     * 订单管理的呃实现类
     */
    @Autowired
    private IOrderService orderService;

    /**
     * cookie中的令牌token：存储用户名的key
     */
    @Value("${XY_TOKEN_KEY}")
    private String XY_TOKEN_KEY;

    @RequestMapping("/order-cart")
    public String showOrder(HttpServletRequest request) {
        // 因为拦截器中以及将用户信息设置到request中，不用再次查询用户信息。

        TbUser user = (TbUser) request.getAttribute("USER_INFO");
        // 展示用户的送货地址：根据用户的 id 查询该用户的配送地址。（这里静态资源）
        // 调用购物车服务，从 redis 中获取该用户的购物车商品列表
        List<TbItem> cartList = cartItemService.queryCartListByUserId(user.getId());
        // 将列表展示到页面中：通过model传递
        request.setAttribute("cartList", cartList);
        return "order-cart";
    }

    /**
     * 创建订单
     * @param orderInfo 订单信息等
     * @param request request
     * @return 返回 success.jsp 页面
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
        // 引入服务
        // 注入服务
        // 补全用户的属性，因为订单拦截器中验证了用户身份，并且将用户信息设置到
        TbUser user = (TbUser) request.getAttribute("USER_INFO");
        // 买家 id
        orderInfo.setUserId(user.getId());
        // 买家名称
        orderInfo.setBuyerNick(user.getUsername());

        // 调用服务
        XiaoyuResult result = orderService.createOrder(orderInfo);

        // 往jsp传递订单完成后页面所需要的数据
        // 订单id
        request.setAttribute("orderId", result.getData());
        // 付款方式
        request.setAttribute("payment", orderInfo.getPayment());
        // 时间工具类
        DateTime dateTime = new DateTime();
        // 当前时间加上 3 天，工具类不用考虑 29、30 等情况
        DateTime plusDays = dateTime.plusDays(3);
        // 到货大概时间
        request.setAttribute("date", plusDays.toString("yyyy-MM-dd"));
        // 创建订货需要把购物车的商品全部清空
        cartItemService.deleteCartItemAllByUserId(user.getId());
        return "success";
    }
}
