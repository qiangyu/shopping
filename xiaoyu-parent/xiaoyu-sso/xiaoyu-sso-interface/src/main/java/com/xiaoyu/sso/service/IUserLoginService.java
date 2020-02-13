package com.xiaoyu.sso.service;

import com.xiaoyu.common.pojo.XiaoyuResult;

/**
 * 登录的接口
 * @author xiaoyu
 * @date 2020/2/11 15:05
 */

public interface IUserLoginService {

    /**
     * 根据用户名和密码登录
     * @param username 用户名
     * @param password 密码
     * @return 返回登录的结果
     *         成功 status:200   data:token(redis的id)
     *         失败 status:400   data:null
     */
    XiaoyuResult login(String username, String password);

    /**
     * 根据令牌 token 来获取用户信息
     * @param token 令牌
     * @return XiaoyuResult的 data 属性包含用户信息
     */
    XiaoyuResult getUserByToken(String token);

}
