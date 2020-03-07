package com.xiaoyu.sso.service.impl;

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
import com.xiaoyu.common.utils.JsonUtils;
import com.xiaoyu.manager.mapper.TbUserMapper;
import com.xiaoyu.manager.pojo.TbUser;
import com.xiaoyu.manager.pojo.TbUserExample;
import com.xiaoyu.sso.service.IUserLoginService;
import com.xiaoyu.sso.service.jedis.IJedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * 登录的实现类
 * @author xiaoyu
 * @date 2020/2/11 15:11
 */

@Service
public class UserLoginServiceImpl implements IUserLoginService {

    /**
     * 注入查询 TbUser 表的 mapper
     */
    @Autowired
    private TbUserMapper userMapper;

    /**
     * jedis客户端，操作redis
     */
    @Autowired
    private IJedisClient jedisClient;

    /**
     * 用户信息token的前缀
     */
    @Value("${USER_INFO}")
    private String USER_INFO;

    /**
     * token的有效期
     */
    @Value("${TOKEN_EXPIRE_TIME}")
    private Integer TOKEN_EXPIRE_TIME;

    /**
     * 根据用户名和密码登录
     * @param username 用户名
     * @param password 密码
     * @return 返回登录的结果
     *         成功 status:200   data:token(redis的id)
     *         失败 status:400   data:null
     */
    @Override
    public XiaoyuResult login(String username, String password) {
        // 注入mapper
        // 校验用户名和密码是否为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return XiaoyuResult.build(400, "用户名或密码错误.");
        }

        // 先校验用户名是否存在
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        // select * from tb_user where username = ?
        List<TbUser> users = userMapper.selectByExample(example);
        // 用户名不存在   当查询不到数据，返回的是：[] 且 size = 0
        if (users == null || users.size() == 0) {
            return XiaoyuResult.build(400, "用户名或密码错误.");
        }
        // 再校验密码是否正确
        TbUser user = users.get(0);
        // 先对传过来的password加密再比较
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 取反  如果密码不正确
        if (!md5Password.equals(user.getPassword())) {
            return XiaoyuResult.build(400, "用户名或密码错误.");
        }

        // 校验成功，生成 token：uuid 存放在redis中的 key
        String token = UUID.randomUUID().toString();
        // 存放用户数据到redis中 ，用户信息等存放在 redis   （key：token  value：用户的JSON数据）
        // 使用jedis客户端，为了管理方便，给 key 加一个前缀："xxx:token"
        // 设置密码为null，防止密码泄露
        user.setPassword(null);
        jedisClient.set(USER_INFO + ":" + token, JsonUtils.objectToJson(user));
        // 设置token有效期来模拟session
        jedisClient.expire(USER_INFO + ":" + token, TOKEN_EXPIRE_TIME);
        // 把 token 设置 cookie 中，在表现层设置
        return XiaoyuResult.ok(token);
    }

    /**
     * 根据cookie里的令牌 token 从redis获取用户信息
     * @param token 令牌
     * @return XiaoyuResult的 data 属性包含用户信息
     */
    @Override
    public XiaoyuResult getUserByToken(String token) {
        // 注入jedis客户端
        // 根据 key（token）查询出用户的信息（json格式的）
        String userInfo = jedisClient.get(USER_INFO + ":" + token);
        // 判断是否查询到用户信息
        if (StringUtils.isNotBlank(userInfo)) {
            // 查询到用户信息（需要返回XiaoyuResult：200，用户信息等，把用户信息转为对象）
            TbUser user = JsonUtils.jsonToPojo(userInfo, TbUser.class);
            // 重新设置过期时间
            jedisClient.expire(USER_INFO + ":" + token, TOKEN_EXPIRE_TIME);
            return XiaoyuResult.ok(user);
        }
        // 查询不到信息，返回400
        return XiaoyuResult.build(400, "用户一过期.");
    }
}
