package com.xiaoyu.search.mapper;

import com.xiaoyu.common.pojo.SearchItem;

import java.util.List;

/**
 * @author xiaoyu
 * @description 从数据库中查询出要 搜索的商品数据 的接口。业务层可以将这些查询出来的数据导入到search索引库中。
 * @date 2020/2/3 21:53
 */

public interface ISearchItemMapper {

    /**
     * @author xiaoyu
     * @description 查询出tb_item a, tb_item_cat b, tb_item_desc c 这三张表的所有商品数据。
     * @date 22:00 2020/2/3
     * @return java.util.List<SearchItem>
     **/
    List<SearchItem> getSearchItemList();

    /**
     * 根据 商品的id 查询 tb_item a, tb_item_cat b, tb_item_desc c 这三张表的商品数据。
     * @param itemId 商品id
     * @return 查询出来的商品信息
     */
    SearchItem getSearchItemById(Long itemId);
}
