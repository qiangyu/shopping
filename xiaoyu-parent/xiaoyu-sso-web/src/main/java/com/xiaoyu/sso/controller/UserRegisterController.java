package com.xiaoyu.sso.controller;

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
import com.xiaoyu.manager.pojo.TbUser;
import com.xiaoyu.sso.service.IUserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 校验用户注册
 * @author xiaoyu
 * @date 2020/2/10 23:55
 */

@Controller
@RequestMapping("/user")
public class UserRegisterController {

    /**
     * 校验用户注册的接口
     */
    @Autowired
    private IUserRegisterService userRegisterService;

    /**
     * 根据参数和类型来校验数据
     * @param param 校验的数据
     * @param type  1、2、3 ，分别代表 username、phone、email
     * @return 返回校验的结果
     *         如果查询到数据----数据不可用：false
     *         如果查询不到数据----数据可用：true
     */
    @RequestMapping(value = "/check/{param}/{type}", method = RequestMethod.GET)
    @ResponseBody
    public XiaoyuResult checkData(@PathVariable String param, @PathVariable Integer type) {
        // 引入服务
        // 注入
        // 调用方法，并返回校验结果
        return userRegisterService.checkData(param, type);
    }

    /**
     * 用户注册
     * @param user 用户填写的信息
     * @return 返回注册结果
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public XiaoyuResult register(TbUser user) {
        // 调用方法，并返回校验结果
        return userRegisterService.register(user);
    }

}
