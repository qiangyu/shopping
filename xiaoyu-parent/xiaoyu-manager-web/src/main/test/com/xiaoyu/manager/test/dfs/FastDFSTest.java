package com.xiaoyu.manager.test.dfs;

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

import com.xiaoyu.manager.util.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;


/**
 * 
 * @author xiaoyu
 * @description 测试使用fastdfs的Java客户端上传图片
 * @date 2020/1/16 12:27
 */

public class FastDFSTest {

    /**
     * 测试上传图片到服务器
     *      图片存储在linux：/home/fastdfs/store_path0/data/00/00
     * @throws Exception
     */
    @Test
    public void testFileUpload() throws Exception {
        // 1、加载配置文件，配置文件中的内容就是tracker服务的地址。
        ClientGlobal.init("C:/Users/Yuqiang/IdeaProjects/shopping/xiaoyu-parent/xiaoyu-manager-web/src/main/resources/properties/client.conf");
        // 2、创建一个TrackerClient对象。直接new一个。
        TrackerClient trackerClient = new TrackerClient();
        // 3、使用TrackerClient对象创建连接，获得一个TrackerServer对象。
        TrackerServer trackerServer = trackerClient.getConnection();
        // 4、创建一个StorageServer的引用，值为null
        StorageServer storageServer = null;
        // 5、创建一个StorageClient对象，需要两个参数TrackerServer对象、StorageServer的引用
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        // 6、使用StorageClient对象上传图片。
        //扩展名不带“.”
        // 图片存储在linux：/home/fastdfs/store_path0/data/00/00
        String[] strings = storageClient.upload_file("C:/Users/Yuqiang/IdeaProjects/shopping/xiaoyu-parent/xiaoyu-manager-web/src/main/resources/images/girl8.jpg", "jpg", null);
        // 7、返回数组。包含组名和图片的路径。
        for (String string : strings) {
            System.out.println(string);
        }
    }

    /**
     * 使用工具类上传图片
     * @throws Exception
     */
    @Test
    public void testFastDFSClient() throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient("C:/Users/Yuqiang/IdeaProjects/shopping/xiaoyu-parent/xiaoyu-manager-web/src/main/resources/properties/client.conf");
        String file = fastDFSClient.uploadFile("C:/Users/Yuqiang/IdeaProjects/shopping/xiaoyu-parent/xiaoyu-manager-web/src/main/resources/images/girl8.jpg");
        System.out.println(file);
    }

}
