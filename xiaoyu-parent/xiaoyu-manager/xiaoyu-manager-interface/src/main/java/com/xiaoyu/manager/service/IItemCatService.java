package com.xiaoyu.manager.service;


import com.xiaoyu.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * 后台管理：类目列表接口
 * @author xiaoyu
 * @date 2020/1/15 14:10
 */

public interface IItemCatService {

    /**
     * 根据父节点的id，查询该节点的子类目列表
     * @param parenId 父节点的id
     * @return 返回节点信息列表
     */
    List<EasyUITreeNode> getItemCatList(Long parenId);

}
