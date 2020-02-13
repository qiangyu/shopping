package com.xiaoyu.sso.service;

import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.manager.pojo.TbUser;

/**
 * 校验用户注册的接口
 * @author xiaoyu
 * @date 2020/2/10 22:50
 */

public interface IUserRegisterService {

    /**
     * 根据参数和类型来校验数据
     * @param param 校验的数据
     * @param type  1、2、3 ，分别代表 username、phone、email
     * @return 返回校验的结果
     *         如果查询到数据----数据不可用：false
     *         如果查询不到数据----数据可用：true
     */
    XiaoyuResult checkData(String param, Integer type);

    /**
     * 注册用户
     * @param tbUser 用户填写的信息
     * @return 返回注册结果
     */
    XiaoyuResult register(TbUser user);

}
