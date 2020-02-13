package com.xiaoyu.search.service.impl;

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

import com.xiaoyu.common.pojo.SearchItem;
import com.xiaoyu.common.pojo.SearchResult;
import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.search.dao.ISearchDao;
import com.xiaoyu.search.mapper.ISearchItemMapper;
import com.xiaoyu.search.service.ISearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作索引库，对索引库增删改查。
 * @author xiaoyu
 * @date 2020/2/3 22:12
 */

@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private ISearchItemMapper searchItemMapper;

    /**
     * 注入HttpSolrServer对象
     */
    @Autowired
    private SolrServer solrServer;

    @Autowired
    private ISearchDao searchDao;

    /**
     * 将查询出来的商品信息导入到索引库
     * @return
     * @throws Exception
     */
    @Override
    public XiaoyuResult importAllItem() throws Exception {
        // 注入ISearchItemMapper
        List<SearchItem> itemList = searchItemMapper.getSearchItemList();
        // 注入HttpSolrServer对象
        // 为每个商品创建一个SolrInputDocument对象 将每个商品信息添加到域里
        for (SearchItem searchItem : itemList) {
            // 创建SolrInputDocument
            SolrInputDocument document = new SolrInputDocument();
            // 将商品的信息添加到各个域
            // id域是字符串   getId()是Long，转字符串
            document.addField("id", searchItem.getId().toString());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            document.addField("item_desc", searchItem.getItem_desc());

            // 向索引库中添加文档
            solrServer.add(document);
        }
        // 提交
        solrServer.commit();
        // 返回成功信息
        return XiaoyuResult.ok();
    }

    /**
     * 删除索引库全部索引
     * @return
     * @throws Exception
     */
    @Override
    public XiaoyuResult deleteAllItem() throws Exception {
        // 注入HttpSolrServer对象
        // 设置条件  删除所有索引
        solrServer.deleteByQuery("*:*");
        // 提交
        solrServer.commit();
        // 返回成功信息
        return XiaoyuResult.ok();
    }

    /**
     * 商城首页，搜索商品时。根据用户查询的条件搜索查询结果
     * @param queryString   查询的主条件
     * @param page  查询当前的页码
     * @param rows  每页显示的行数  controller中写死
     * @return  查询出来的结果集
     * @throws Exception
     */
    @Override
    public SearchResult search(String queryString, Integer page, Integer rows) throws Exception {
        // 1、创建一个SolrQuery对象
        SolrQuery query = new SolrQuery();
        // 2、设置主查询条件
        // 判断用户搜索的信息是不是空字符串
        if (StringUtils.isNoneBlank(queryString)) {
            // 非空
            query.setQuery(queryString);
        } else {
            query.setQuery("*:*");
        }
        // 3、设置过滤条件：分页条件
        // 如果不传页数（page）和每页显示的行数（rows）过来，涉案值默认值
        if (page == null || page == 0) {
            page = 1;
        }
        if (rows == null || rows == 0) {
            rows = 60;
        }
        query.setStart((page - 1) * rows);
        query.setRows(rows);
        // 4、指定默认搜索域
        query.set("df", "item_title");
        // 5、设置高亮
        query.setHighlight(true);
        // 设置高亮显示的域
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        // 6、执行查询，调用SearchDao 得到 SearchResult
        SearchResult result = searchDao.search(query);
        // 7、计算总页数
        // 获取总记录数
        long recordCount = result.getRecordCount();
        // 总记录数出每页行数得出总页数
        long pageCount = recordCount / rows;
        if (recordCount % rows > 0) {
            // 如果
            ++pageCount;
        }
        result.setPageCount(pageCount);
        // 8、返回SearchResult
        return result;
    }

    /**
     * 据商品的id查询商品的数据，并且更新到索引库中。
     * @param itemId 商品id
     * @return
     * @throws Exception
     */
    @Override
    public XiaoyuResult updateSearchItemById(Long itemId) throws Exception {
        return searchDao.updateSearchItemById(itemId);
    }
}
