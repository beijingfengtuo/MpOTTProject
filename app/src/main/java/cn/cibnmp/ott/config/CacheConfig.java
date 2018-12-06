package cn.cibnmp.ott.config;

/**
 * Created by wanqi on 2017/3/25.
 */

public class CacheConfig {

    /**
     * 缓存30分钟,30*60
     */
    public final static int cache_half_hour = 30 * 60;

    /**
     * 缓存1个小时
     */
    public final static int cache_an_hour = 60 * 60;


    /**
     * 缓存半天
     */
    public final static int cache_half_day = 12 * 60 * 60;

    /**
     * 缓存1天
     */
    public final static int cache_one_day = 24 * 60 * 60;


    /**
     * 无限期缓存
     */
    public final static int cache_indefinitely = -1;

    /**
     * 不缓存，直接从网络获取数据
     */
    public final static int cache_no_cache = 0;
}
