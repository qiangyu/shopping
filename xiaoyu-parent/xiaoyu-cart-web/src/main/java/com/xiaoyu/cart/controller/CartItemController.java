package com.xiaoyu.cart.controller;

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
import com.xiaoyu.common.utils.JsonUtils;
import com.xiaoyu.manager.pojo.TbItem;
import com.xiaoyu.manager.pojo.TbUser;
import com.xiaoyu.manager.service.IItemService;
import com.xiaoyu.sso.service.IUserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理购物车
 * 每个方法都有大量重复的代码
 *
 * @author xiaoyu
 * @date 2020/2/12 21:25
 */
@Controller
@RequestMapping("/cart")
public class CartItemController {

    /**
     * 用于查询商品信息
     */
    @Autowired
    private IItemService itemService;

    /**
     * SSO服务，获取用户信息
     */
    @Autowired
    private IUserLoginService userLoginService;

    /**
     * 购物车的操作
     */
    @Autowired
    private ICartItemService cartItemService;

    /**
     * 存放在cookie中商品列表的 key
     */
    @Value("${XY_COOKIE_CART_KEY}")
    private String XY_COOKIE_CART_KEY;

    /**
     * 存放在cookie的过期时间
     */
    @Value("${XY_CART_EXPIRE_TIME}")
    private Integer XY_CART_EXPIRE_TIME;


    /**
     * 根据商品的ID，数量加购物车，并展示 “商品已成功加入购物车！” 的页面（cartSuccess.jsp）
     *
     * @param itemId   哪一个商品（商品id）
     * @param num    购买商品的数量
     * @return cartSuccess.jsp页面
     */
    @RequestMapping("/add/{itemId}")
    public String addCart(@PathVariable Long itemId, Integer num,
                          HttpServletRequest request, HttpServletResponse response) {
        // 从cookie中获取 token（key），用来从redis取出用户的信息
        String token = CookieUtils.getCookieValue(request, "XY_TOKEN");

        // 根据token调用SSO服务，获取用户的信息
        XiaoyuResult result = userLoginService.getUserByToken(token);
        // 判断，如果status=200，说明用户存在并且已经登录
        if (result.getStatus() == 200) {
            // 先调用商品服务的方法，获取商品的数据
            TbItem item = itemService.getItemById(itemId);
            // 获取用户的 id
            TbUser user = (TbUser) result.getData();
            Long userId = user.getId();
            // 添加购物车的方法，将数据添加到redis中
            cartItemService.addCartItem(userId, item, num);
        } else {
            // 表示用户不存在，说明用户没登录。将商品信息存放于 cookie 中
            this.addCookieCartItem(itemId, num, request, response);
        }
        return "cartSuccess";
    }



    /**
     * 购物车展示列表的页面（cart.jsp）
     * @param request request
     * @return
     */
    @RequestMapping("/cart")
    public String showCart(HttpServletRequest request) {
        // 判断用户是否登录
        // 从cookie中获取 token（key），用来从redis取出用户的信息
        String token = CookieUtils.getCookieValue(request, "XY_TOKEN");

        // 根据token调用SSO服务，获取用户的信息
        XiaoyuResult result = userLoginService.getUserByToken(token);
        // 如果用户已登录
        if (result.getStatus() == 200) {
            // 用户已登录
            TbUser user = (TbUser) result.getData();
            // 查询出购物车所有的商品数据
            List<TbItem> items = cartItemService.queryCartListByUserId(user.getId());
            request.setAttribute("cartList", items);
        } else {
            // 用户没登录
            List<TbItem> items = this.getCartList(request);
            request.setAttribute("cartList", items);
        }
        return "cart";
    }

    /**
     * 根据用户的ID和商品的ID进行更新购物车商品数量
     * @param itemId 商品id
     * @param num 商品数量
     * @param request request
     * @param response response
     * @return 返回操作结果
     */
    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody
    public XiaoyuResult updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response) {
        // 判断用户是否登录
        // 从cookie中获取 token（key），用来从redis取出用户的信息
        String token = CookieUtils.getCookieValue(request, "XY_TOKEN");

        // 根据token调用SSO服务，获取用户的信息
        XiaoyuResult result = userLoginService.getUserByToken(token);

        // 如果用户已登录
        if (result.getStatus() == 200) {
            // 用户已登录
            TbUser user = (TbUser) result.getData();
            // 据用户的ID和商品的ID进行更新商品数量
            cartItemService.updateCartItemByUserIdAndItemId(user.getId(), itemId, num);
        } else {
            // 用户没登录
            this.updateCookieCartItemNum(itemId, num, request, response);
        }
        return XiaoyuResult.ok();
    }

    /**
     * 根据商品的id和用户的id进行删除购物车商品
     * @param itemId 商品id
     * @param request request
     * @param response response
     * @return 重定向：redirect:/cart/cart.html
     */
    @RequestMapping("/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {

        // 判断用户是否登录
        // 从cookie中获取 token（key），用来从redis取出用户的信息
        String token = CookieUtils.getCookieValue(request, "XY_TOKEN");

        // 根据token调用SSO服务，获取用户的信息
        XiaoyuResult result = userLoginService.getUserByToken(token);

        // 如果用户已登录
        if (result.getStatus() == 200) {
            // 用户已登录
            TbUser user = (TbUser) result.getData();
            // 据用户的ID和商品的ID进行删除商品
            cartItemService.deleteCartItemByUserIdAndItemId(user.getId(), itemId);
        } else {
            // 用户没登录
            this.deleteCookieCartItem(itemId, request, response);
        }
        return "redirect:/cart/cart.html";
    }

    /**
     * 将商品添加到 cookie
     * @param itemId 商品id
     * @param num 商品数量
     * @param request request
     * @param response response
     */
    private void addCookieCartItem(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        // 从cookie获取商品列表
        List<TbItem> items = getCartList(request);
        // 判断，要添加的商品是否存在于cookie的商品列表中
        boolean flag = this.updateItemsNum(items, itemId, num);
        // 如果商品不存在商品列表中
        if (flag) {
            // 如果商品在商品列表，更新数量。最后，重新设置到cookie中
        } else {
            // 调用商品服务查询商品数据
            TbItem item = itemService.getItemById(itemId);
            // 设置商品里面的属性（num原来是库存数量， 设置成传过来要加入购物车的商品数量、图片取一张即可）
            item.setNum(num);
            if (StringUtils.isNotBlank(item.getImage())) {
                item.setImage(item.getImage().split(",")[0]);
            }
            // 将商品信息存放于购物车列表中
            items.add(item);
            // 最后，将商品的列表转成JSON存储到cookie中  key ：XY_COOKIE_CART_ITEM ,value:列表的JSON
        }
        // 无论要不要加购物车的商品存不存在商品列表中，都将商品的列表转成JSON存储到cookie中  key ：XY_COOKIE_CART_ITEM ,value:列表的JSON
        CookieUtils.setCookie(request, response, XY_COOKIE_CART_KEY, JsonUtils.objectToJson(items), XY_CART_EXPIRE_TIME, true);
    }

    /**
     * 从cookie中获取商品列表
     * @param request request
     * @return 返回商品列表信息
     */
    private List<TbItem> getCartList(HttpServletRequest request) {
        // 从cookie中获取商品列表json数据
        String itemJson = CookieUtils.getCookieValue(request, XY_COOKIE_CART_KEY, true);
        // 如果从 cookie 获取到商品的json数据
        List<TbItem> items = new ArrayList<>();
        if (StringUtils.isNotBlank(itemJson)) {
            // 将json数据转化成list集合，返回
            items = JsonUtils.jsonToList(itemJson, TbItem.class);
        }
        return items;
    }

    /**
     * 通过cookie，更新用户的购物车：修改购物车商品数量
     * @param itemId 商品id
     * @param num 加购物车商品数量
     * @param request request
     * @param response response
     */
    private void updateCookieCartItemNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        List<TbItem> items = this.getCartList(request);
        // 判断，要添加的商品是否存在于cookie的商品列表中
        boolean flag = this.updateItemsNum(items, itemId, num);
        if (flag) {
            // 设置cookie，将商品的列表转换成JSON设置回cookie中
            CookieUtils.setCookie(request, response, XY_COOKIE_CART_KEY, JsonUtils.objectToJson(items), XY_CART_EXPIRE_TIME, true);
        } else {
            // 购物车都没有这商品，还改锤子商品数量。
        }
    }

    /**
     * 如果列表中有商品的id和itemId一致 ，说明商品找到，更新商品的数量
     * @param items 商品列表
     * @param itemId 价购物车的商品id
     * @param num 价购物的商品数量
     * @return 返回在商品列表中是否找到 传递过来的商品（itemId）
     *          找到：true
     *          找到：false
     */
    private boolean updateItemsNum(List<TbItem> items, Long itemId, Integer num) {
        // 判断，要添加的商品是否存在于cookie的商品列表中
        boolean flag = false;
        for (TbItem item : items) {
            // 如果商品id相同
            if (item.getId() == itemId.longValue()) {
                // 商品数量加 num
                item.setNum(num);
                return true;
            }
        }
        return false;
    }

    /**
     * 通过cookie，更新用户的购物车：删除购物车的商品
     * @param itemId
     * @param request
     * @param response
     */
    private void deleteCookieCartItem(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        //1.从cookie中获取商品的列表
        List<TbItem> items = getCartList(request);
        // 判断，要添加的商品是否存在于cookie的商品列表中
        boolean flag = false;
        for (TbItem item : items) {
            // 如果商品id相同
            if (item.getId() == itemId.longValue()) {
                // 商品数量加 num
                items.remove(item);
                flag = true;
                break;
            }
        }
        if (flag) {
            // 如果商品在商品列表中，删除该商品，重新设置购物车列表到cookie中
            CookieUtils.setCookie(request, response, XY_COOKIE_CART_KEY, JsonUtils.objectToJson(items), XY_CART_EXPIRE_TIME, true);
        } else {
            // 商品列表都没有商品，你删除个锤子。
        }
    }

}
