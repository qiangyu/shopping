package com.xiaoyu.portal.pojo;

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

/**
 * 传给首页大广告位轮播图的数据（POJO）
 * @author xiaoyu
 * @date 2020/1/31 20:05
 */
 
public class IndexCategoryAd {
    /**
     * 大广告位图片
     */
    private String src;
    private String srcB;

    /**
     * 大广告位图片的高
     */
    private String height;
    private String heightB;

    /**
     * 大广告位图片的宽
     */
    private String width;
    private String widthB;

    /**
     * SubTitle中获取，，，标题
     */
    private String alt;

    /**
     * url
     */
    private String href;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrcB() {
        return srcB;
    }

    public void setSrcB(String srcB) {
        this.srcB = srcB;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeightB() {
        return heightB;
    }

    public void setHeightB(String heightB) {
        this.heightB = heightB;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWidthB() {
        return widthB;
    }

    public void setWidthB(String widthB) {
        this.widthB = widthB;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
