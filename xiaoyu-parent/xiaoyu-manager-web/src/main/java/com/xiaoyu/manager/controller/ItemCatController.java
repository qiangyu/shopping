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

import com.xiaoyu.common.pojo.EasyUITreeNode;
import com.xiaoyu.manager.service.IItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 后台管理：商品类目的选择
 * @author xiaoyu
 * @date 2020/1/16 9:45
 */

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

    /**
     * 引入后台管理类目列表实现类
     */
    @Autowired
    private IItemCatService itemCatService;

    /**
     * 后台管理类目录的展示
     * @param parenId 父目录id：为零时代表是一级目录
     * @return 返回根据父目录id查到同一级的目录
     */
    @RequestMapping("/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(@RequestParam(value = "id", defaultValue = "0") Long parenId) {
        // 引入服务
        // 注入服务
        // 调用方法 获取到树节点信息
        List<EasyUITreeNode> list = itemCatService.getItemCatList(parenId);
        // 返回节点信息列表
        return list;
    }
}
