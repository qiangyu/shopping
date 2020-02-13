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
import com.xiaoyu.content.service.IContentService;
import com.xiaoyu.manager.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台内容处理的controller
 * @author xiaoyu
 * @date 2020/1/18 12:16
 */

@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private IContentService contentService;

    /**
     * @author xiaoyu
     * @description $.post("/content/save",$("#contentAddForm").serialize(), function(data)
     * @date 12:21 2020/1/18
     * @param content
     * @return XiaoyuResult 返回值是json
     **/
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public XiaoyuResult saveContent(TbContent content) {
        // 引入服务
        // 注入服务
        // 调用方法
        return contentService.saveContent(content);
    }

}
