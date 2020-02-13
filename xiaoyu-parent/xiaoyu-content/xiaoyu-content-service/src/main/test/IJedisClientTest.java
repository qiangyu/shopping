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

import com.xiaoyu.content.jedis.IJedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author xiaoyu
 * @description 测试不改变源码，通过spring注入不同实体类。来切换单机或集群的redis
 * @date 2020/2/2 0:35
 */
 
public class IJedisClientTest {

    /**
     * @author xiaoyu
     * @description 测试不改变代码，通过改变注入的实体类来切换单机或群集的redis
     *                  在 applicationContext-redis.xml 中改变注入的实体类
     * @date 0:43 2020/2/2
     * @param
     * @return void
     **/
    @Test
    public void testRedis() {
        // 初始化spring容器 ，，加载配置文件
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        // 获取bean  需要通过接口getBean(Class<T> requiredType)来获取Bean
        IJedisClient bean = ac.getBean(IJedisClient.class);
        // 调用方法
        System.out.println(bean.get("hello"));
    }
}
