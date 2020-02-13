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

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author xiaoyu
 * @description 使用Jedis测试redis
 * @date 2020/2/1 21:27
 */
 
public class JedisTest {
    /**
     * @author xiaoyu
     * @description 使用Jedis测试单击版redis   没有使用连接池
     * @date 21:34 2020/2/1
     * @param
     * @return void
     **/
    @Test
    public void testRedis() {
        // 创建Jedis对象，指定服务端的ip以及端口号
        Jedis jedis = new Jedis("192.168.25.128", 6379);
        // 使用jedis操作redis，每个redis命令对应一个方法
        jedis.set("hello", "word");
        System.out.println(jedis.get("hello"));
        // 关闭jedis
        jedis.close();
    }

    /**
     * @author xiaoyu
     * @description 使用Jedis测试单击版redis   使用连接池
     * @date 21:34 2020/2/1
     * @param
     * @return void
     **/
    @Test
    public void testRedisPool() {
        // 创建JedisPool对象 并且制定服务端ip及端口号
        JedisPool pool = new JedisPool("192.168.25.128", 6379);
        // 从连接池获取jedis
        Jedis jedis = pool.getResource();
        // 使用jedis操作redis，每个redis命令对应一个方法
        jedis.set("pool", "pool");
        System.out.println(jedis.get("pool"));
        // 关闭jedis(还会连接池中)
        jedis.close();
        // 关闭连接池   (一般系统关闭是关闭)
        pool.close();
    }


    /**
     * @author xiaoyu
     * @description 使用Jedis测试集群版redis  JedisCluster里面封装有连接池
     * @date 21:34 2020/2/1
     * @param
     * @return void
     **/
    @Test
    public void testClusterRedis() {
        // 使用JedisCluster对象。需要一个Set<HostAndPort>参数。Redis节点的列表
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.25.128", 7001));
        nodes.add(new HostAndPort("192.168.25.128", 7002));
        nodes.add(new HostAndPort("192.168.25.128", 7003));
        nodes.add(new HostAndPort("192.168.25.128", 7004));
        nodes.add(new HostAndPort("192.168.25.128", 7005));
        nodes.add(new HostAndPort("192.168.25.128", 7006));
        JedisCluster cluster = new JedisCluster(nodes);
        // 直接使用JedisCluster对象操作redis，和单击版一样
        cluster.set("hello", "cluster");
        System.out.println(cluster.get("hello"));
        // 关闭JedisCluster对象   （一般都是系统关闭时关闭）
    }
}
