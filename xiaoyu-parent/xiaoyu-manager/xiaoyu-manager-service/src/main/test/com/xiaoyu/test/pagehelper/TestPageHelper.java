package com.xiaoyu.test.pagehelper;

/**
 * .::::.
 * .::::::::.
 * :::::::::::    佛主保佑、永无Bug
 * ..:::::::::::'
 * '::::::::::::'
 * .::::::::::
 * '::::::::::::::..
 * ..::::::::::::.
 * ``::::::::::::::::
 * ::::``:::::::::'        .:::.
 * ::::'   ':::::'       .::::::::.
 * .::::'      ::::     .:::::::'::::.
 * .:::'       :::::  .:::::::::' ':::::.
 * .::'        :::::.:::::::::'      ':::::.
 * .::'         ::::::::::::::'         ``::::.
 * ...:::           ::::::::::::'              ``::.
 * ```` ':.          ':::::::::'                  ::::..
 * '.:::::'                    ':'````..
 */

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.manager.mapper.TbItemMapper;
import com.xiaoyu.manager.mapper.TbUserMapper;
import com.xiaoyu.manager.pojo.TbItem;
import com.xiaoyu.manager.pojo.TbItemExample;
import com.xiaoyu.manager.pojo.TbUser;
import com.xiaoyu.manager.pojo.TbUserExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author xiaoyu
 * @description 测试分页
 * @date 2020/1/14 21:11
 */

public class TestPageHelper {

    /**
     * 测试分页
     */
    @Test
    public void testPage() {
        // 1、初始化spring容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        // 2、获取mapper的代理对象
        TbItemMapper itemMapper = ac.getBean(TbItemMapper.class);
        // 3、设置分页信息
        PageHelper.startPage(1, 3); // 第一页有三条数据  紧跟着的第一个查询才会被分页
        // 4、调用 方法
        TbItemExample example = new TbItemExample(); // 设置查询条件的使用
        List<TbItem> list = itemMapper.selectByExample(example); // 没有设置条件：select * from tb_item
        List<TbItem> list2 = itemMapper.selectByExample(example); // 没有设置条件：select * from tb_item

        // 取分页信息
        PageInfo<TbItem> info = new PageInfo<>(list);

        System.out.println("第一个分页list的集合长度：" + list.size());
        System.out.println("第一个分页list2的集合长度：" + list2.size());

        // 5、遍历结果集

        System.out.println("查询总记录条数" + info.getTotal());

        for (TbItem tbItem : list) {
            System.out.println(tbItem.getTitle());
        }

    }

    /**
     * 测试查询不到数据返回值是什么
     */
    @Test
    public void test() {
        // 1、初始化spring容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        // 2、获取mapper的代理对象
        TbUserMapper tbUserMapper = ac.getBean(TbUserMapper.class);
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo("1231423141241");
        // 调用方法查询
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);

        System.out.println(tbUsers);
        System.out.println(tbUsers.size());

    }

}
