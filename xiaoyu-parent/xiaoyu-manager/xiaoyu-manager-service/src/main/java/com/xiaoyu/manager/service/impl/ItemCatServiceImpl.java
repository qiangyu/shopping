package com.xiaoyu.manager.service.impl;

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
import com.xiaoyu.manager.mapper.TbItemCatMapper;
import com.xiaoyu.manager.pojo.TbItemCat;
import com.xiaoyu.manager.pojo.TbItemCatExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台管理：类目列表实现类
 * @author xiaoyu
 * @date 2020/1/15 14:14
 */

@Service
public class ItemCatServiceImpl implements IItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    /**
     * 根据父节点的id，查询该节点的子类目列表
     * @param parenId 父节点的id
     * @return 返回节点信息列表
     */
    @Override
    public List<EasyUITreeNode> getItemCatList(Long parenId) {
        // 注入mapper
        // 设置条件，根据父类节点的parenId查询该节点的子类列表.
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        // 当父目录id为零时，代表的是一级目录
        criteria.andParentIdEqualTo(parenId);

        // 调用方法.
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        // 转换成EasyUITreeNode列表。
        ArrayList<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbItemCat itemCat : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(itemCat.getId());
            node.setText(itemCat.getName());
            node.setState(itemCat.getIsParent()? "closed" : "open");

            // 添加到列表
            resultList.add(node);
        }
        return resultList;
    }
}
