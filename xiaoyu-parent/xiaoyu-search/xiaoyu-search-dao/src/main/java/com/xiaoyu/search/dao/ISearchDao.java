package com.xiaoyu.search.dao;

import com.xiaoyu.common.pojo.SearchResult;
import com.xiaoyu.common.pojo.XiaoyuResult;
import org.apache.solr.client.solrj.SolrQuery;

/**
 * @author xiaoyu
 * @description 从索引库中搜索商品信息的接口
 * @date 2020/2/4 14:45
 */

public interface ISearchDao {

    /**
     * 从索引库中搜索符合条件的商品信息的结果集
     * @param query
     * @return 返回操作的结果
     * @throws Exception
     */
    SearchResult search(SolrQuery query) throws Exception;

    /**
     * 根据商品id，从数据库查询信息，然后更新索引库的信息
     * @param itemId 商品id
     * @return 返回操作的结果
     * @throws Exception
     */
    XiaoyuResult updateSearchItemById(Long itemId) throws Exception;

}
