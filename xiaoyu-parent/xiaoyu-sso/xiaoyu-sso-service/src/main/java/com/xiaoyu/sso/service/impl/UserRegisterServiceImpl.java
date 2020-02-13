package com.xiaoyu.sso.service.impl;

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

import com.xiaoyu.common.pojo.XiaoyuResult;
import com.xiaoyu.manager.mapper.TbUserMapper;
import com.xiaoyu.manager.pojo.TbUser;
import com.xiaoyu.manager.pojo.TbUserExample;
import com.xiaoyu.sso.service.IUserRegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 校验用户注册的实现类
 * @author xiaoyu
 * @date 2020/2/10 22:58
 */

@Service
public class UserRegisterServiceImpl implements IUserRegisterService {

    /**
     * 注入查询 TbUser 表的 mapper
     */
    @Autowired
    private TbUserMapper userMapper;

    /**
     * 根据参数和类型来校验数据
     *
     * @param param 校验的数据
     * @param type  1、2、3 ，分别代表 username、phone、email
     * @return 返回校验的结果
     * 如果查询到数据----数据不可用：false
     * 如果查询不到数据----数据可用：true
     */
    @Override
    public XiaoyuResult checkData(String param, Integer type) {
        // 注入mapper
        // 根据type来动态的生成查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        switch (type) {
            case 1:
                // username  用户名不能为 null
                if (StringUtils.isEmpty(param)) {
                    return XiaoyuResult.ok(false);
                }
                criteria.andUsernameEqualTo(param);
                break;
            case 2:
                // phone
                criteria.andPhoneEqualTo(param);
                break;
            case 3:
                // email
                criteria.andEmailEqualTo(param);
                break;
            default:
                // 非法参数  400(不好的请求)
                return XiaoyuResult.build(400, "非法参数");
        }
        // 调用方法查询  当查询不到数据，返回的是：[] 且 size = 0
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        // 如果查询到数据----数据不可用：false
        if (tbUsers != null && tbUsers.size() > 0) {
            return XiaoyuResult.ok(false);
        }
        // 如果查询不到数据----数据可用：true
        return XiaoyuResult.ok(true);
    }

    /**
     * 用户注册
     * @param user 用户填写的信息
     * @return 返回注册结果
     */
    @Override
    public XiaoyuResult register(TbUser user) {
        // 注入mapper
        // 校验数据
        // 用户名密码不能为空
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getUsername())) {
            return XiaoyuResult.build(400, "注册失败. 请校验数据后在提交数据.");
        }

        // 校验 username、phone、email 三个是否被注册返回的结果
        XiaoyuResult result = null;

        // 校验用户名是否被注册
        result = this.checkData(user.getUsername(), 1);
        if (!(Boolean) result.getData()) {
            // 用户名被注册
            return XiaoyuResult.build(400, "注册失败. 请校验数据后在提交数据.");
        }

        // 校验电话号码是否被注册
        if (StringUtils.isNotBlank(user.getPassword())) {
            result = null;
            result = this.checkData(user.getPhone(), 2);
            if (!(Boolean) result.getData()) {
                // 电话号码被注册
                return XiaoyuResult.build(400, "注册失败. 请校验数据后在提交数据.");
            }
        }

        // 校验email是否被注册
        if (StringUtils.isNotBlank(user.getEmail())) {
            result = null;
            result = this.checkData(user.getEmail(), 3);
            if (!(Boolean) result.getData()) {
                // email被注册
                return XiaoyuResult.build(400, "注册失败. 请校验数据后在提交数据.");
            }
        }

        // 校验成功，补全属性  注册的日期等
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        // 对密码进行MD5加密
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        // 插入数据
        userMapper.insertSelective(user);
        // 返回注册结果 XiaoyuResult
        return XiaoyuResult.ok();
    }
}
