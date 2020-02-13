package com.xiaoyu.portal.controller;

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

import com.xiaoyu.common.utils.JsonUtils;
import com.xiaoyu.content.service.IContentService;
import com.xiaoyu.manager.pojo.TbContent;
import com.xiaoyu.portal.pojo.IndexCategoryAd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示首页
 * @author xiaoyu
 * @date 2020/1/17 14:05
 */

@Controller
public class PageController {

    @Autowired
    private IContentService contentService;

    /**
     * 首页大广告位的分类id
     */
    @Value("${INDEX_AD_CATEGORY_ID}")
    private Long categoryId;

    /**
     * 大广告位图片A的高
     */
    @Value("${INDEX_AD_HEIGHT}")
    private String height;

    /**
     * 大广告位图片B的高
     */
    @Value("${INDEX_AD_HEIGHT_B}")
    private String heightB;

    /**
     * 大广告位图片A的宽
     */
    @Value("${INDEX_AD_WIDTH}")
    private String width;

    /**
     * 大广告位图片B的宽
     */
    @Value("${INDEX_AD_WIDTH_B}")
    private String widthB;

    /**
     * @author xiaoyu
     * @description 展示首页 
     * @date 19:48 2020/1/31
     * @param  
     * @return java.lang.String 
     **/
    @RequestMapping("/index")
    public String showIndex(Model model) {
        // 引入服务
        // 注入服务
        // 根据内容分类的id，查询该内容列表
        List<TbContent> list = contentService.getContentListByCatId(categoryId);
        // 转成自定义的POJO列表(封装到IndexCategoryAd类中)

//        private String src;
//        private String srcB;
//        private String height;
//        private String heightB;
//        private String width;
//        private String widthB;
//        private String alt;
//        private String href;

        List<IndexCategoryAd> nodes = new ArrayList<>();
        for (TbContent content : list) {
            IndexCategoryAd ad = new IndexCategoryAd();
            // 标题
            ad.setAlt(content.getSubTitle());

            // 大广告位图片的高
            ad.setHeight(height);
            ad.setHeightB(heightB);

            // url
            ad.setHref(content.getUrl());

            // 图片
            ad.setSrc(content.getPic());
            ad.setSrcB(content.getPic2());

            // 大广告位图片的宽
            ad.setWidth(width);
            ad.setWidthB(widthB);

            nodes.add(ad);
        }
        // 传递数据给前端  JsonUtils.objectToJson(nodes)将对象转为json数据
        model.addAttribute("ad1", JsonUtils.objectToJson(nodes));
        return "index";
    }

}
