package com.xiaoyu.manager.test.solr;

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

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

/**
 * 
 * @author xiaoyu
 * @description 测试使用solrJ管理索引库
 * @date 2020/2/4 0:09
 */
 
public class SolrJTest {

    /**
     * 测试使用solrJ添加索引库
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void testAddDocument() throws IOException, SolrServerException {
        // 创建一个SolrServer, 使用HttpSolrServer创建对象
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr");
        // 创建一个文档对象SolrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        // 向文档中添加域。必须有id域，域的名称必须在schema.xml中定义
        document.addField("id", "test01");
        document.addField("item_title", "测试添加");
        // 把文档添加到索引库中
        solrServer.add(document);
        // 提交
        solrServer.commit();
    }

}
