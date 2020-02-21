package com.xiaoyu.order.service;

import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.order.pojo.OrderInfo;

/**
 * 管理订单的接口
 *
 * @author xiaoyu
 * @date 2020/2/14 11:33
 */
public interface IOrderService {

    /**
     * 创建订单
     * @param orderInfo 订单信息
     * @return 返回订单号，用XiaoyuResult的data属性返回
     */
    XiaoyuResult createOrder(OrderInfo orderInfo);

}
