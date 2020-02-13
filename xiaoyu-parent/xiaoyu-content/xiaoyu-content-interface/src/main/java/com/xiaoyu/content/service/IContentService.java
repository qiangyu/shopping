package com.xiaoyu.content.service;

import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.manager.pojo.TbContent;

import java.util.List;

/**
 * 后台内容处理的接口
 * @author xiaoyu
 * @date 2020/1/18 11:55
 */

public interface IContentService {

    /**
     * @author xiaoyu
     * @description 新增内容
     * @date 12:08 2020/1/18
     * @param content 传过来的表单内容
     * @return XiaoyuResult
     **/
    XiaoyuResult saveContent(TbContent content);

    /**
     * @author xiaoyu
     * @description 根据内容分类的id，查询该内容列表
     * @date 17:45 2020/1/31
     * @param categoryId 
     * @return java.util.List<TbContent>
     **/
    List<TbContent> getContentListByCatId(Long categoryId);

}
