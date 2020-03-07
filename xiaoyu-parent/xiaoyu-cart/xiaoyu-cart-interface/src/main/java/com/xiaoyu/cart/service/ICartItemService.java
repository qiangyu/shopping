package com.xiaoyu.cart.service;

import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.manager.pojo.TbItem;

import java.util.List;

/**
 * 购物车相关接口
 *
 * @author xiaoyu
 * @date 2020/2/12 16:26
 */

public interface ICartItemService {

    /**
     * 根据用户ID和商品的ID从redis中查询购物车商品的信息
     * @param userId 哪一个用户的购物车（用户id）
     * @param item 哪一个商品
     * @param num 购买商品的数量
     * @return XiaoyuResult的data包含视频信息
     */
    XiaoyuResult addCartItem(Long userId, TbItem item, Integer num);

    /**
     * 根据用户ID和商品的ID查询购物车是否存储在redis中
     * @param userId 用户id
     * @param itemId 商品id
     * @return null 说明不存在，如果不为空说明存在
     */
    TbItem queryTbItemByUserAndItemId(Long userId, Long itemId);

    /**
     * 根据用户名从redis中查询购物车商品列表
     * @param userId 用户名
     * @return List<TbItem>
     */
    List<TbItem> queryCartListByUserId(Long userId);

    /**
     * 根据用户的ID和商品的ID进行更新购物车商品数量
     * @param userId 用户id
     * @param itemId 商品id
     * @param num 商品数量
     * @return 返回操作结果
     */
    XiaoyuResult updateCartItemByUserIdAndItemId(Long userId, Long itemId, Integer num);

    /**
     * 根据商品的id和用户的id进行删除购物车商品
     * @param userId 用户id
     * @param itemId 商品id
     * @return 返回操作结果
     */
    XiaoyuResult deleteCartItemByUserIdAndItemId(Long userId, Long itemId);

    /**
     * 根据用户 id 删除购物车所有商品
     * @param userId 用户 id
     * @return 操作结果
     */
    XiaoyuResult deleteCartItemAllByUserId(Long userId);

}
