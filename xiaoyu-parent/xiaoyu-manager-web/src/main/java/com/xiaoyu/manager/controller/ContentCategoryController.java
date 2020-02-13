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
import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.content.service.IContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 后台内容分类管理处理的controller
 * @author xiaoyu
 * @date 2020/1/17 18:05
 */

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

    @Autowired
    private IContentCategoryService contentCategoryService;

    /**
     * 请求的url：/content/category/list
     * 请求的参数：id，当前节点的id。第一次请求是没有参数，需要给默认值“0”
     * 响应数据：List<EasyUITreeNode>（@ResponseBody）
     * 响应的结果：Json数据。[{id:1,text:节点名称,state:open(closed)},
     *                      {id:2,text:节点名称2,state:open(closed)},
     *                      {id:3,text:节点名称3,state:open(closed)}]
     * 通过节点的id查询该节点的列表
     * @param parentId  查询节点id
     * @return          返回节点列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        // 1、引入服务
        // 2、注入服务
        // 3、调用服务
        System.out.println(parentId);
        return contentCategoryService.getContentCategoryList(parentId);
    }

    /**
     * 请求的url：/content/category/create
     * 请求的参数：Long parentId   String name
     * 响应的结果：json数据，XiaoyuResult
     * 添加内容分类（新创建的节点）
     * @param parentId  父节点的id
     * @param name      新增节点的名称
     * @return          返回执行后的状态等
     */
    @RequestMapping("/create")
    @ResponseBody
    public XiaoyuResult createContentCategory(Long parentId, String name) {
        // 1、引入服务
        // 2、注入服务
        // 3、调用服务
        return contentCategoryService.createContentCategory(parentId, name);
    }

}
