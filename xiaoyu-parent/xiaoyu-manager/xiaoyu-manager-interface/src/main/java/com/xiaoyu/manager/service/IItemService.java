package com.xiaoyu.manager.service;

import com.xiaoyu.common.pojo.EasyUIDataGridResult;
import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.manager.pojo.TbItem;
import com.xiaoyu.manager.pojo.TbItemDesc;


/**
 * 处理后台查询商品相关的service接口
 * @author xiaoyu
 * @date 2020/1/14 23:16
 */

public interface IItemService {

    /**
     * 根据当前的页码和每页的行数进行分页查询
     * @param page  页码
     * @param rows  每页的行数
     * @return      json数据
     */
    EasyUIDataGridResult getItemList(Integer page, Integer rows);

    /**
     * 根据商品的基础数据、商品的描述信息，插入商品（插入商品基础表、商品描述表）
     * @param item 使用TbItem对象接收表单的商品基本数据。
     * @param desc 使用字符串接收表单中的商品描述的数据。
     * @return
     */
    XiaoyuResult saveItem(TbItem item, String desc);

    /**
     * 根据商品的id查询商品基本信息
     * @param itemId 商品id
     * @return 返回商品基本信息
     */
    TbItem getItemById(Long itemId);

    /**
     * 根据商品的id查询商品描述信息
     * @param itemId 商品id
     * @return 返回商品描述信息
     */
    TbItemDesc getItemDescById(Long itemId);
}
