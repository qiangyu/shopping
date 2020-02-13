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

import com.xiaoyu.manager.mapper.ITestMapper;
import com.xiaoyu.manager.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author xiaoyu
 * @date 2020/1/13 23:34
 */

@Service
public class TestServiceImpl implements ITestService {

    @Autowired
    private ITestMapper mapper;

    @Override
    public String queryNow() {
        // 1、注入mapper
        // 2、调用mapper的方法 返回
        return mapper.queryNow();
    }
}
