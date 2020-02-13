package com.xiaoyu.content.service.impl;

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
import com.xiaoyu.common.utils.JsonUtils;
import com.xiaoyu.content.service.IContentService;
import com.xiaoyu.content.service.jedis.IJedisClient;
import com.xiaoyu.manager.mapper.TbContentMapper;
import com.xiaoyu.manager.pojo.TbContent;
import com.xiaoyu.manager.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台内容处理的实现类
 * @author xiaoyu
 * @date 2020/1/18 12:09
 */

@Service
public class ContentServiceImpl implements IContentService {

    /**
     * 操作content表格的dao层实体类
     */
    @Autowired
    private TbContentMapper contentMapper;

    /**
     * 操作redis的实体类，用接口接收
     */
    @Autowired
    private IJedisClient client;

    @Value("${CONTENT_AD_KEY}")
    private String CONTENT_AD_KEY;

    /**
     * @author xiaoyu
     * @description 新增内容分类的信息
     * @date 12:13 2020/1/18
     * @param content 传过来的表单内容
     * @return XiaoyuResult
     **/
    @Override
    public XiaoyuResult saveContent(TbContent content) {
        // 1、注入mapper
        // 2、补全其他属性
        content.setCreated(new Date());
        content.setUpdated(content.getCreated());
        // 3、插入内容到表中
        contentMapper.insertSelective(content);

        // 在新增内容的时候，需要将该内容分类下的所有缓存清空
        try {
            client.hdel(CONTENT_AD_KEY, content.getCategoryId() + "");
            System.out.println("当新增内容的时，清空缓存...");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return XiaoyuResult.ok();
    }

    /**
     * @author xiaoyu
     * @description 根据内容分类的id，查询该内容列表
     * @date 19:46 2020/1/31
     * @param categoryId
     * @return java.util.List<TbContent>
     **/
    @Override
    public List<TbContent> getContentListByCatId(Long categoryId) {

        // 添加缓存时不能影响正常的业务逻辑

        // 判断redis中是否有数据，如果有数据则直接从redis里面拿，不再去mysql数据库查
        // 从redis里获取 key 为 CONTENT_AD_KEY 的数据
        try {
            String jsonstr = client.hget(CONTENT_AD_KEY, categoryId + "");
            // jsonstr不为空时
            if (StringUtils.isNoneBlank(jsonstr)) {
                System.out.println("从redis缓存中拿数据...");
                // 直接返回内容分类下categoryId的所有信息列表
                return JsonUtils.jsonToList(jsonstr, TbContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1、注入mapper
        // 2、创建example
        TbContentExample example = new TbContentExample();
        // 3、设置查询条件
        example.createCriteria().andCategoryIdEqualTo(categoryId);
        // 4、执行查询
        List<TbContent> list = contentMapper.selectByExample(example);

        // 表示没有缓存，从mysql数据库中拿到数据后，将数据存入redis中
        // 注入IJedisClient
        // 调用方法写入redis   用hash  key-field-value
        try {
            client.hset(CONTENT_AD_KEY, categoryId + "", JsonUtils.objectToJson(list));
            System.out.println("将数据存储到redis...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回
        return list;
    }
}
