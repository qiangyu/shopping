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

import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.search.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 导入数据到solr索引库
 * @author xiaoyu
 * @date 2020/2/3 23:40
 */

@Controller
@RequestMapping("/index")
public class SearchItemController {

    @Autowired
    private ISearchService searchService;

    /**
     * 导入 搜索商品的信息 的solr索引库
     * @return
     */
    @RequestMapping("/importAll")
    @ResponseBody
    public XiaoyuResult importAll() {
        try {
            return searchService.importAllItem();
        } catch (Exception e) {
            e.printStackTrace();
            return XiaoyuResult.build(500, "导入数据失败。");
        }
    }

    /**
     * 删除索引库里的全部索引
     * @return
     */
    @RequestMapping("/deleteAll")
    @ResponseBody
    public XiaoyuResult deleteAll() {
        try {
            return searchService.deleteAllItem();
        } catch (Exception e) {
            e.printStackTrace();
            return XiaoyuResult.build(500, "删除全部索引失败。");
        }
    }
}
