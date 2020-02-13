package com.xiaoyu.search.controller;

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

import com.xiaoyu.common.pojo.SearchResult;
import com.xiaoyu.search.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

/**
 * 接收首页搜索的请求
 * @author xiaoyu
 * @date 2020/2/5 12:33
 */

@Controller
public class SearchController {

    @Autowired
    private ISearchService searchService;

    /**
     * 搜索页面 每页的行数
     */
    @Value("${SEARCH_ROWS}")
    private Integer SEARCH_ROWS;

    /**
     * 商城首页搜索商品信息
     * @param queryString   用户搜索条件
     * @param page  页数
     * @return  返回搜索页面search.jsp
     */
    @RequestMapping("/search")
    public String search(@RequestParam(value = "q") String queryString,
                         @RequestParam(defaultValue="1") Integer page, Model model) {
        // 引入访问
        // 注入ISearchService
        // 调用方法
        // 处理queryString乱码
        try {
            queryString = new String(queryString.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SearchResult searchResult = null;
        try {
            searchResult = searchService.search(queryString, page, SEARCH_ROWS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 设置数据传递到搜索页面search.jsp中
        model.addAttribute("query", queryString);
        model.addAttribute("totalPages", searchResult.getRecordCount());
        model.addAttribute("itemList", searchResult.getItemList());
        model.addAttribute("page", page);

        return "search";
    }

}
