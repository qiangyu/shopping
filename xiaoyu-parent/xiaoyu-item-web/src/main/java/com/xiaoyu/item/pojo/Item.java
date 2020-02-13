package com.xiaoyu.item.pojo;

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

import com.xiaoyu.manager.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * 商品的详细信息
 * @author xiaoyu
 * @date 2020/2/8 11:54
 */
 
public class Item extends TbItem {

    /**
     * 如果有两张图片，则将它们分离开
     * @return
     */
    public String[] getImages() {
        String image = this.getImage();
        // 如果image不等于空
        if (StringUtils.isNoneBlank(image)) {
            return image.split(",");
        }
        return null;
    }

    public Item(TbItem tbItem) {
        // 将tbItem的属性值拷贝到当前对象item里（注意：只有相同的属性才能拷贝）
        // copyProperties(源对象, 目标对象);
        BeanUtils.copyProperties(tbItem, this);
    }
}
