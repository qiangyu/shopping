package com.xiaoyu.order.service.jedis;


import java.util.Map;

/**
 * 操作redis的接口，分别有单机和集群的实体类继承该接口，
 * 可以在不改变service层代码的情况下，通过spring注入的实体类（单机和集群的实体类）不同，可以切换单机和集群的redis
 * @author xiaoyu
 */
public interface IJedisClient {

	/**
	 * @author xiaoyu
	 * @description 设置字符串类型 key value 
	 * @date 22:53 2020/2/1
	 * @param key key
 	 * @param value value
	 * @return java.lang.String 
	 **/
	String set(String key, String value);

	/**
	 * @author xiaoyu
	 * @description 根据字符串类型的key获取value 
	 * @date 22:54 2020/2/1
	 * @param key key
	 * @return java.lang.String 
	 **/
	String get(String key);
	
	/**
	 * @author xiaoyu
	 * @description 判断key是否存在 
	 * @date 22:54 2020/2/1
	 * @param key key
	 * @return java.lang.Boolean 
	 **/
	Boolean exists(String key);
	
	/**
	 * @author xiaoyu
	 * @description 过期时间设置
	 * @date 22:55 2020/2/1
	 * @param key key
 	 * @param seconds 过期时间（秒）
	 * @return java.lang.Long 
	 **/
	Long expire(String key, int seconds);
	
	/**
	 * @author xiaoyu
	 * @description 看还剩多少时间过期 
	 * @date 22:56 2020/2/1
	 * @param key key
	 * @return java.lang.Long 
	 **/
	Long ttl(String key);
	
	/**
	 * @author xiaoyu
	 * @description 将key中储存的数字值增一
	 * @date 22:56 2020/2/1
	 * @param key
	 * @return java.lang.Long 
	 **/
	Long incr(String key);
	
	/**
	 * @author xiaoyu
	 * @description 啊
	 * @date 0:12 2020/2/2
	 * @param key
     * @Param field
     * @Param value
	 * @return java.lang.Long
	 **/
	Long hset(String key, String field, String value);

	/**
	 * @author xiaoyu
	 * @description 获取hash类型的值
	 * @date 22:59 2020/2/1
	 * @param key
 	 * @param field
	 * @return java.lang.String
	 **/
	String hget(String key, String field);

	/**
	 * 根据key获取前部field value
	 * @param key
	 * @return
	 */
	Map<String, String> hgetAll(String key);

	/**
	 * @author xiaoyu
	 * @description 删除key
	 * @date 0:11 2020/2/2
	 * @param key
     * @Param field
	 * @return java.lang.Long
	 **/
	Long hdel(String key, String... field);
	
}
