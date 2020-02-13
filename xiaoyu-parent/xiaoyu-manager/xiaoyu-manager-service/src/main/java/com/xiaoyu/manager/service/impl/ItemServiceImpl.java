package com.xiaoyu.manager.service.impl;

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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoyu.common.pojo.EasyUIDataGridResult;
import com.xiaoyu.common.pojo.IDUtils;
import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.common.utils.JsonUtils;
import com.xiaoyu.manager.mapper.TbItemDescMapper;
import com.xiaoyu.manager.mapper.TbItemMapper;
import com.xiaoyu.manager.pojo.TbItem;
import com.xiaoyu.manager.pojo.TbItemDesc;
import com.xiaoyu.manager.pojo.TbItemExample;
import com.xiaoyu.manager.service.IItemService;
import com.xiaoyu.manager.service.jedis.IJedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

/**
 * 处理后台查询商品相关的实现类
 * @author xiaoyu
 * @date 2020/1/14 23:24
 */

@Service
public class ItemServiceImpl implements IItemService {

    /**
     * 商品基本信息表
     */
    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 商品描述表
     */
    @Autowired
    private TbItemDescMapper itemDescMapper;

    /**
     * 接收消息和发送消息时使用的类
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 发布/订阅的方式发送消息
     */
    @Resource(name = "topicDestination")
    private Destination topicDestination;

    /**
     * 操作redis
     */
    @Autowired
    private IJedisClient jedisClientPool;

    @Value("${ITEM_INFO_KEY}")
    private String ITEM_INFO_KEY;

    @Value("${ITEM_INFO_KEY_EXPIRE}")
    private Integer ITEM_INFO_KEY_EXPIRE;

    /**
     * 根据当前的页码和每页的行数进行分页查询
     * @param page  页码
     * @param rows  每页的行数
     * @return
     */
    @Override
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        // 1、设置分页信息 使用PageHelper
        if (page == null || page == 0) {
            page = 1;
        }
        if (rows == null || rows == 0) {
            rows = 30;
        }
        PageHelper.startPage(page, rows);
        // 2、注入mapper
        // 3、创建example对象 不需要设置查询条件
        TbItemExample example = new TbItemExample();
        // 4、根据mapper调用查询所有数据的方法
        List<TbItem> list = itemMapper.selectByExample(example);
        // 5、获取分页信息
        PageInfo<TbItem> info = new PageInfo<>(list);
        // 6、封装到EasyUIDataGridResult
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int) info.getTotal());
        result.setRows(info.getList());
        // 7、返回
        return result;
    }

    /**
     * 后台管理新增商品
     * @param item 使用TbItem对象接收表单的商品基本数据。
     * @param desc 使用字符串接收表单中的商品描述的数据。
     * @return 返回新增商品的状态（成功或失败）
     */
    @Override
    public XiaoyuResult saveItem(TbItem item, String desc) {
        // 1、生成商品的id
        final long itemId = IDUtils.genItemId();
        // 2、补全TbItem对象的属性
        item.setId(itemId);
        // 商品的状态：1（正常）  2（下架）  3（删除）
        item.setStatus((byte) 1);
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);
        // 3、向商品表插入数据
        itemMapper.insert(item);
        // 4、创建一个TbItemDesc(商品描述表对应的pojo)对象
        TbItemDesc itemDesc = new TbItemDesc();
        // 5、补全TbItemDesc的属性
        itemDesc.setItemId(itemId);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        // 商品的描述
        itemDesc.setItemDesc(desc);
        // 6、向商品描述表插入数据
        itemDescMapper.insert(itemDesc);

        // 添加发送消息到ActiveMQ的业务逻辑
        jmsTemplate.send(topicDestination, (Session session)-> session.createTextMessage(itemId + ""));

        System.out.println("新增商品成功！");
        // 5、返回成功的消息XiaoyuResult.ok
        return XiaoyuResult.ok();
    }

    /**
     * 据商品的id查询商品基本信息
     * @param itemId 商品id
     * @return
     */
    @Override
    public TbItem getItemById(Long itemId) {

        // 添加缓存不能影响正常的业务逻辑，因此要捕获异常

        try {
            // 从缓存中获取数据，如果有则直接返回
            String jsonstr = jedisClientPool.get(ITEM_INFO_KEY + ":" + itemId + ":BASE");
            // 如果缓存中有数据
            if (StringUtils.isNoneBlank(jsonstr)) {
                // 设置过期时间
                jedisClientPool.expire(ITEM_INFO_KEY + ":" + itemId + ":BASE", ITEM_INFO_KEY_EXPIRE);

                System.out.println("从缓存中获取tbItem...");

                return JsonUtils.jsonToPojo(jsonstr, TbItem.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 注入mapper
        // 调用方法
        // 返回tbitem
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);

        // 如果查询有数据，则添加缓存
        try {
            if (tbItem != null) {
                // 添加缓存
                jedisClientPool.set(ITEM_INFO_KEY + ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
                // 设置过期时间
                jedisClientPool.expire(ITEM_INFO_KEY + ":" + itemId + ":BASE", ITEM_INFO_KEY_EXPIRE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tbItem;
    }

    /**
     * 根据商品的id查询商品描述信息
     * @param itemId 商品id
     * @return 返回商品描述信息
     */
    @Override
    public TbItemDesc getItemDescById(Long itemId) {

        // 添加缓存不能影响正常的业务逻辑，因此要捕获异常

        try {
            // 从缓存中获取数据，如果有则直接返回
            String jsonstr = jedisClientPool.get(ITEM_INFO_KEY + ":" + itemId + ":DESC");
            // 如果缓存中有数据
            if (StringUtils.isNoneBlank(jsonstr)) {
                // 设置过期时间
                jedisClientPool.expire(ITEM_INFO_KEY + ":" + itemId + ":DESC", ITEM_INFO_KEY_EXPIRE);

                System.out.println("从缓存中获取tbItemDesc...");

                return JsonUtils.jsonToPojo(jsonstr, TbItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);

        // 如果查询有数据，则添加缓存
        try {
            if (tbItemDesc != null) {
                // 添加缓存
                jedisClientPool.set(ITEM_INFO_KEY + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
                // 设置过期时间
                jedisClientPool.expire(ITEM_INFO_KEY + ":" + itemId + ":DESC", ITEM_INFO_KEY_EXPIRE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tbItemDesc;
    }
}
