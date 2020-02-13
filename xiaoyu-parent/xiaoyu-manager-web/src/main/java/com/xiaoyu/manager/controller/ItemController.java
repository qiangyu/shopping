package com.xiaoyu.manager.controller;

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

import com.xiaoyu.common.pojo.EasyUIDataGridResult;
import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.manager.pojo.TbItem;
import com.xiaoyu.manager.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台管理查询商品数据
 * @author xiaoyu
 * @date 2020/1/14 23:41
 */

@Controller
@RequestMapping("/item")
public class ItemController {

    /**
     * 引入处理后台查询商品相关的实现类
     */
    @Autowired
    private IItemService itemService;

    /**
     * url:/item/list
     * method:get
     * 后台管理查询商品的信息
     * @param page  页码
     * @param rows  每页的行数
     * @return      json
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        // 1、引用服务
        // 2、注入服务
        // 3、调用服务方法
        return itemService.getItemList(page, rows);
    }

    /**
     * 后台管理新增商品
     * @param item 商品基本信息
     * @param desc 商品的描述
     * @return 返回新增商品的状态（成功或失败）
     */
    @RequestMapping("/save")
    @ResponseBody
    public XiaoyuResult saveItem(TbItem item, String desc) {
        XiaoyuResult result = itemService.saveItem(item, desc);
        return result;
    }
}
