package com.xiaoyu.search.service;

import com.xiaoyu.common.pojo.SearchResult;
import com.xiaoyu.common.pojo.XiaoyuResult;


/**
 * 操作索引库，增删改查。
 * @author xiaoyu
 * @date 2020/2/3 22:07
 */

public interface ISearchService {

    /**
     * 将查询出来的所有要 搜索的商品数据 导入到索引库的接口。
     * @return
     * @throws Exception
     */
    XiaoyuResult importAllItem() throws Exception;

    /**
     * 删除索引库全部索引
     * @return
     * @throws Exception
     */
    XiaoyuResult deleteAllItem() throws Exception;

    /**
     * 根据查询的条件搜索查询结果
     * @param queryString   查询的主条件
     * @param page  查询当前的页码
     * @param rows  每页显示的行数  controller中写死
     * @return  查询出来的结果集
     * @throws Exception
     */
    SearchResult search(String queryString, Integer page, Integer rows) throws Exception;

    /**
     * 根据商品的id查询商品的数据，并且更新到索引库中。
     * @param itemId 商品id
     * @return  返回操作的结果
     * @throws Exception
     */
    XiaoyuResult updateSearchItemById(Long itemId) throws Exception;
}
