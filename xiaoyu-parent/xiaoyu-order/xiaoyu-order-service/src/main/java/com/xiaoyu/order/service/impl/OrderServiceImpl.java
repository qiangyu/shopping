package com.xiaoyu.order.service.impl;

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
import com.xiaoyu.manager.mapper.TbOrderItemMapper;
import com.xiaoyu.manager.mapper.TbOrderMapper;
import com.xiaoyu.manager.mapper.TbOrderShippingMapper;
import com.xiaoyu.manager.pojo.TbOrderItem;
import com.xiaoyu.manager.pojo.TbOrderShipping;
import com.xiaoyu.order.pojo.OrderInfo;
import com.xiaoyu.order.service.IOrderService;
import com.xiaoyu.order.service.jedis.IJedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 订单管理的实现类
 * 
 * @author xiaoyu
 * @date 2020/2/14 11:35
 */
@Service
public class OrderServiceImpl implements IOrderService {

    /**
     * 订单表
     */
    @Autowired
    private TbOrderMapper orderMapper;

    /**
     * 订单项表
     */
    @Autowired
    private TbOrderItemMapper orderItemMapper;

    /**
     * 订单物流
     */
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;

    /**
     * 操作redis的客户端jedis
     */
    @Autowired
    private IJedisClient jedisClient;

    /**
     * redis里的订单 id 的 key
     */
    @Value("${GEN_ORDER_ID_KEY}")
    private String GEN_ORDER_ID_KEY;

    /**
     * redis里的订单 id 初始值的 key
     */
    @Value("${GEN_ORDER_ID_INIT}")
    private String GEN_ORDER_ID_INIT;

    /**
     * 订单项的主键 id 的 key
     */
    @Value("${GEN_ORDER_ITEM_ID_KEY}")
    private String GEN_ORDER_ITEM_ID_KEY;

    /**
     * 创建订单
     * @param orderInfo 订单信息
     * @return 返回订单号，用XiaoyuResult的data属性返回
     */
    @Override
    public XiaoyuResult createOrder(OrderInfo orderInfo) {
        // 1、插入订单表
        // 通过redis的 incr 命令生成订单的 id
        // 取反，如果key不存在，需要给key一个初始值
        if (!jedisClient.exists(GEN_ORDER_ID_KEY)) {
            jedisClient.set(GEN_ORDER_ID_KEY, GEN_ORDER_ID_INIT);
        }
        // 如果 key 存在了：Redis Incr 命令将 key 中储存的数字值增一。
        String orderId = jedisClient.incr(GEN_ORDER_ID_KEY).toString();
        // 补全其他属性
//        orderInfo.setUserId(); // 用户id，由controller接收到设置
//        orderInfo.setBuyerNick(); // 卖家名称，controller接收数据设置
        orderInfo.setCreateTime(new Date()); // 订单创建时间
        orderInfo.setUpdateTime(orderInfo.getCreateTime()); // 订单更新时间
        orderInfo.setOrderId(orderId); // 订单id
        orderInfo.setPostFee("0"); // 邮费
        orderInfo.setStatus(1); // 1：未付款    2：已付款   3：未发货   4：已发货  .....
        orderMapper.insert(orderInfo);

        ///2、插入订单项表
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            // 通过redis的 incr 命令生成订单项的 id
            String orderItemId = jedisClient.incr(GEN_ORDER_ITEM_ID_KEY).toString();
            orderItem.setId(orderItemId);
            orderItem.setOrderId(orderId);
            // 插入订单项表
            orderItemMapper.insert(orderItem);
        }

        ///3、插入订单物流
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        // 设置订单 id
        orderShipping.setOrderId(orderId);
        // 补全其他属性
        orderShipping.setCreated(orderInfo.getCreateTime());
        orderShipping.setUpdated(orderInfo.getCreateTime());
        // 插入到物流表
        orderShippingMapper.insert(orderShipping);

        // 返回需要包括订单的 id
        return XiaoyuResult.ok(orderId);
    }
}
