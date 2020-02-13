package com.xiaoyu.item.controller;

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

import com.xiaoyu.item.pojo.Item;
import com.xiaoyu.manager.pojo.TbItem;
import com.xiaoyu.manager.pojo.TbItemDesc;
import com.xiaoyu.manager.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商品详情页面
 * @author xiaoyu
 * @date 2020/2/8 12:01
 */

@Controller
public class ItemController {

    /**
     * 查询商品相关的实现类
     */
    @Autowired
    private IItemService iItemService;

    @RequestMapping("/item/{itemId}")
    public String getItem(@PathVariable Long itemId, Model model){
        // 引入服务
        // 注入服务
        // 调用方法  基本信息、描述信息
            // 商品的基本信息
        TbItem tbItem = iItemService.getItemById(itemId);
            // 商品的描述信息
        TbItemDesc itemDesc = iItemService.getItemDescById(itemId);
        // TbItem 转成 Item
        Item item = new Item(tbItem);
        // 将item、itemDesc对象添加到model域中
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", itemDesc);
        // 返回Item
        return "item";
    }

}
