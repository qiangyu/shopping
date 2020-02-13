package com.xiaoyu.search.dao.impl;

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
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 从索引库中搜索商品信息的dao
 * @author xiaoyu
 * @date 2020/2/4 14:51
 */

@Repository
public class SearchDaoImpl implements ISearchDao {

    /**
     * 由spring注入SolrServer的子类HttpSolrServer
     */
    @Autowired
    private SolrServer solrServer;

    /**
     * 从数据库中查询出要 搜索的商品数据 的接口
     */
    @Autowired
    private ISearchItemMapper searchItemMapper;

    /**
     * 从索引库中搜索符合条件的商品信息的结果集
     * @param query
     * @return
     * @throws Exception
     */
    @Override
    public SearchResult search(SolrQuery query) throws Exception {

        /**
         * 用来 存储从索引库搜索出来的商品分页信息 的对象
         */
        SearchResult searchResult = new SearchResult();

        // 创建 SolrServer 对象
        // 执行查询
        QueryResponse response = solrServer.query(query);
        // 获取结果集
        SolrDocumentList results = response.getResults();
        // 设置查询到商品的总记录数
        searchResult.setRecordCount(results.getNumFound());

        // 创建一个List，存到SearchResult的itemList 属性中
        List<SearchItem> itemList = new ArrayList<>();

        //取高亮显示
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        // 遍历结果集
        for (SolrDocument document : results) {
            // 将SolrDocument的属性 一个个 设置到 SearchItem 中
            SearchItem searchItem = new SearchItem();
            searchItem.setId(Long.parseLong(document.get("id").toString()));
            searchItem.setCategory_name(document.get("item_category_name").toString());
            searchItem.setImage((String) document.get("item_image"));
            // 商品价格
            searchItem.setPrice((Long) document.get("item_price"));
            // 不需要显示item_desc
//            searchItem.setItem_desc((String) document.get("item_desc"));
            // 商品买点
            searchItem.setSell_point((String) document.get("item_sell_point"));

            List<String> list = highlighting.get(document.get("id")).get("item_title");
            String itemTitle = "";
            //有高亮显示的内容时。
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) document.get("item_title");
            }
            searchItem.setTitle(itemTitle);

            // 将 searchItem 添加到List
            itemList.add(searchItem);
        }
        // 设置 SearchResult 的属性   将SearchItem 设置到 SearchResult 的 itemList 属性中
        searchResult.setItemList(itemList);
        return searchResult;
    }

    /**
     * 根据商品id，从数据库查询信息，然后更新索引库的信息
     * @param itemId 商品id
     * @return 返回操作的结果
     * @throws Exception
     */
    @Override
    public XiaoyuResult updateSearchItemById(Long itemId) throws Exception {
        //1.调用mapper中的方法
        SearchItem searchItem = searchItemMapper.getSearchItemById(itemId);
        //2.创建SolrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        //3.向文档中添加域
        document.addField("id", searchItem.getId());
        document.addField("item_title", searchItem.getTitle());
        document.addField("item_sell_point", searchItem.getSell_point());
        document.addField("item_price", searchItem.getPrice());
        document.addField("item_image", searchItem.getImage());
        document.addField("item_category_name", searchItem.getCategory_name());
        document.addField("item_desc", searchItem.getItem_desc());
        //4.添加文档到索引库中
        solrServer.add(document);
        //5.提交
        solrServer.commit();
        return XiaoyuResult.ok();
    }
}