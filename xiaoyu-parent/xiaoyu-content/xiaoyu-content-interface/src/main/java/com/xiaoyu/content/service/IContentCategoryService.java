package com.xiaoyu.content.service;

import com.xiaoyu.common.pojo.EasyUITreeNode;
import com.xiaoyu.common.pojo.XiaoyuResult;

import java.util.List;

/**
 * 后台内容分类管理的树目录
 * @author xiaoyu
 * @date 2020/1/17 17:10
 */

public interface IContentCategoryService {

    /**
     * 通过节点的id查询该节点的列表
     * @param parentId 查询节点id
     * @return 返回节点列表
     */
    List<EasyUITreeNode> getContentCategoryList(Long parentId);

    /**
     * 添加内容分类（新创建的节点）
     * @param parentId  父节点的id
     * @param name      新增节点的名称
     * @return          返回执行后的状态等
     */
    XiaoyuResult createContentCategory(Long parentId, String name);

}
