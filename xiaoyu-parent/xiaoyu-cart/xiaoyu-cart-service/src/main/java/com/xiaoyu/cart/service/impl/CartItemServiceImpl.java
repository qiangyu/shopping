package com.xiaoyu.cart.service.impl;

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
import com.xiaoyu.cart.service.jedis.IJedisClient;
import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.common.utils.JsonUtils;
import com.xiaoyu.manager.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 购物车相关的实现类
 * 存在问题：
 *      每个方法都有大量重复的代码
 *      当没登录时加添加了商品到购物车（购物车的商品列表存在 cookie 里）
 *      登录的时候没将 cookie 里的商品列表存入到redis里面，导致登录后 cookie 里的商品列表丢失
 *      所以，当没登录时，把商品加入了购物车，器购物车结算时，调到订单页面前需要登录，登录完调到订单页面显示的是登录时的购物车列表，而不是 没登录时加到购物车选中的列表
 *      以及好像购物车页面默认是选中全部商品
 *
 * @author xiaoyu
 * @date 2020/2/12 16:32
 */
@Service
public class CartItemServiceImpl implements ICartItemService {

    /**
     * jedis客户端操作redis
     */
    @Autowired
    private IJedisClient jedisClient;

    /**
     * 存储在redis里面用户购物车商品信息的 key 前缀
     */
    @Value("${XY_CART_ITEM_KEY}")
    private String XY_CART_ITEM_KEY;

    /**
     * 根据用户ID和商品的ID从redis中查询购物车商品的信息
     *
     * @param userId 哪一个用户的购物车（用户id）
     * @param item   哪一个商品（商品id）
     * @param num    购买商品的数量
     * @return XiaoyuResult的data包含视频信息
     */
    @Override
    public XiaoyuResult addCartItem(Long userId, TbItem item, Integer num) {
        // 从redis数据库中查询该用户 item.id 商品的信息
        TbItem itemInfo = this.queryTbItemByUserAndItemId(userId, item.getId());
        // 判断是否查询出数据
        if (itemInfo == null) {
            // 该用户的购物车没有该商品，则 将商品直接添加到购物车
            String image = item.getImage();
            if (StringUtils.isNotBlank(image)) {
                // 图片有多张，取一张
                item.setImage(image.split(",")[0]);
            }
            // 设置购买数量 item里面的num原本是库存数量
            item.setNum(num);
        } else {
            // 如果该商品和redis查询出来的商品相同，则数量相加
            item.setNum(item.getNum() + num);
        }
        // 将item转为json，存储到redis里面
        jedisClient.hset(XY_CART_ITEM_KEY + ":" + userId, item.getId() + "", JsonUtils.objectToJson(item));
        return XiaoyuResult.ok();
    }

    /**
     * 根据用户ID和商品的ID查询购物车是否有商品信息存储在redis中
     *
     * @param userId 用户id
     * @param itemId 商品id
     * @return null 说明不存在，如果不为空说明存在
     */
    @Override
    public TbItem queryTbItemByUserAndItemId(Long userId, Long itemId) {
        // 根据 key：userId和 field：itemId 取出redis里面的商品数据
        String hget = jedisClient.hget(XY_CART_ITEM_KEY + ":" + userId, itemId + "");
        // 判断是否查询到数据
        if (StringUtils.isNotBlank(hget)) {
            // 数据不为空，返回商品信息
            return JsonUtils.jsonToPojo(hget, TbItem.class);
        }
        // 数据为null，返回null
        return null;
    }

    /**
     * 根据用户名从redis中查询购物车商品列表
     * @param userId 用户名
     * @return List<TbItem>
     */
    @Override
    public List<TbItem> queryCartListByUserId(Long userId) {
        // 根据用户的ID查询到所有的field的值（map）
        Map<String, String> map = jedisClient.hgetAll(XY_CART_ITEM_KEY + ":" + userId);
        Set<Map.Entry<String, String>> set = map.entrySet();

        // 数据存在
        if (set != null && set.size() > 0) {
            List<TbItem> items = new ArrayList<>();
            // 遍历map对象，将其添加到List中
            for (Map.Entry<String, String> entry : set) {
                TbItem item = JsonUtils.jsonToPojo(entry.getValue(), TbItem.class);
                items.add(item);
            }
            // 返回一个List<TbItem>
            return items;
        }
        return null;
    }

    /**
     * 根据用户的ID和商品的ID进行更改购物车商品数量
     * @param userId 用户id
     * @param itemId 商品id
     * @param num 商品数量
     * @return 返回操作结果
     */
    @Override
    public XiaoyuResult updateCartItemByUserIdAndItemId(Long userId, Long itemId, Integer num) {
        // 从redis数据库中查询该用户 item.id 商品的信息
        TbItem item = this.queryTbItemByUserAndItemId(userId, itemId);
        if (item != null) {
            // 更改购物车商品数量
            item.setNum(num);
            // 重新将新的值设回redis中
            jedisClient.hset(XY_CART_ITEM_KEY + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        }
        return XiaoyuResult.ok();
    }

    /**
     * 根据商品的id和用户的id进行删除购物车商品
     * @param userId 用户id
     * @param itemId 商品id
     * @return 返回操作结果
     */
    @Override
    public XiaoyuResult deleteCartItemByUserIdAndItemId(Long userId, Long itemId) {
        jedisClient.hdel(XY_CART_ITEM_KEY + ":" + userId, itemId + "");
        return XiaoyuResult.ok();
    }

    /**
     * 根据用户 id 删除购物车所有商品
     * @param userId 用户 id
     * @return 操作结果
     */
    @Override
    public XiaoyuResult deleteCartItemAllByUserId(Long userId) {
        // 根据用户名从redis中查询购物车商品列表
        List<TbItem> items = this.queryCartListByUserId(userId);
        // 循环一个个删除
        for (TbItem item : items) {
            this.deleteCartItemByUserIdAndItemId(userId, item.getId());
        }
        return XiaoyuResult.ok();
    }
}
