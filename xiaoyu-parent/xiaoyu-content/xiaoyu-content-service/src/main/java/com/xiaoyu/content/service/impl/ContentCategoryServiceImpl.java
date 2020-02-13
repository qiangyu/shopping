package com.xiaoyu.content.service.impl;

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
import com.xiaoyu.manager.mapper.TbContentCategoryMapper;
import com.xiaoyu.manager.pojo.TbContentCategory;
import com.xiaoyu.manager.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台内容分类管理的树目录
 * @author xiaoyu
 * @date 2020/1/17 17:42
 */

@Service
public class ContentCategoryServiceImpl implements IContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    /**
     * 通过节点的id查询该节点的列表
     * @param parentId 节点id
     * @return
     */
    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
        // 1、注入mapper
        // 2、创建example
        TbContentCategoryExample example = new TbContentCategoryExample();
        // 3、设置条件
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        // 相当于：select * from tb_content_category where parent_id = parentId;
        criteria.andParentIdEqualTo(parentId);
        // 4、执行查询
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        // 5、转成EasyUITreeNode列表
        List<EasyUITreeNode> nodes = new ArrayList<>();
        for (TbContentCategory tbContentCategory : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent() ? "closed" : "open");

            // 加到list
            nodes.add(node);
        }
        // 6、返回
        return nodes;
    }

    /**
     * 新增节点
     * @param parentId  父节点的id
     * @param name      新增节点的名称
     * @return          返回XiaoyuResult，其中有新增节点id（TbContentCategory对象）
     */
    @Override
    public XiaoyuResult createContentCategory(Long parentId, String name) {
        // 创建一个TbContentCategory对象
        TbContentCategory tbContentCategory = new TbContentCategory();
        // 补全TbContentCategory对象的属性
        // 新增的节点不是父节点，是叶子节点
        tbContentCategory.setIsParent(false);
        tbContentCategory.setName(name);
        tbContentCategory.setParentId(parentId);
        // 排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
        tbContentCategory.setSortOrder(1);
        // 状态。可选值:1(正常),2(删除)
        tbContentCategory.setStatus(1);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(tbContentCategory.getCreated());
        // 向tb_content_category表中插入数据
        contentCategoryMapper.insertSelective(tbContentCategory);

        // 判断父节点的is_parent是否为false(判断新增节点的父节点是否为叶子节点)：true是父节点，false是叶子节点
        // 如果新增节点的父节点为叶子节点，则要将父节点的is_parent属性改为true
        TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parentNode.getIsParent()) {
            // 更新父节点的is_parent属性为true
            parentNode.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parentNode);
        }
        // 返回XiaoyuResult，其中有新增节点id（TbContentCategory对象）
        return XiaoyuResult.ok(tbContentCategory);
    }
}
