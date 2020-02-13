package com.xiaoyu.manager.controller;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 后台管理：新增商品时图片上传
 * @author xiaoyu
 * @date 2020/1/16 19:49
 */

@Controller
@RequestMapping("/pic")
public class PictureController {

    @Value("${XIAOYU_IMAGE_SERVER_URL}")
    private String XIAOYU_IMAGE_SERVER_URL;

    /**
     * 后台管理：新增商品时上传图片在图片服务器
     * @param uploadFile 上传的图片
     * @return 返回上传是否成功的信息
     */
    @RequestMapping("/upload")
    @ResponseBody
    public Map<?, ?> fileUpload(MultipartFile uploadFile) {
        try {
            // 取得文件扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            // 创建一个FastDFS的客户端
            FastDFSClient client = new FastDFSClient("classpath:resource/client.conf");
            // 执行上传
            String path = client.uploadFile(uploadFile.getBytes(), extName);
            // 拼接返回的IP地址和url，拼接成完整的url
            String url = XIAOYU_IMAGE_SERVER_URL + path;

            // 返回Map
            Map result = new HashMap<>(2);
            result.put("error", 0);
            result.put("url", url);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            // 返回Map
            Map result = new HashMap<>(2);
            result.put("error", 1);
            result.put("message", "图片上传失败");
            return result;
        }
    }
}
