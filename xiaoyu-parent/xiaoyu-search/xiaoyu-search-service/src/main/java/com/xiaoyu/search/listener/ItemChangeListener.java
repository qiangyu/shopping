package com.xiaoyu.search.listener;

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

import com.xiaoyu.search.service.impl.SearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 监听器，监听ActiveMQ是否有消息，有就接收。
 * @author xiaoyu
 * @date 2020/2/8 0:13
 */
 
public class ItemChangeListener implements MessageListener {

    /**
     * 注入操作索引库实体类类，对索引库增删改查。
     */
    @Autowired
    private SearchServiceImpl searchService;

    /**
     * 如果接收到商品id，则根据商品id更新索引库。
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = null;
            Long itemId = null;
            //取商品id
            if (message instanceof TextMessage) {
                textMessage = (TextMessage) message;
                itemId = Long.parseLong(textMessage.getText());
            }
            //向索引库添加文档
            searchService.updateSearchItemById(itemId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
