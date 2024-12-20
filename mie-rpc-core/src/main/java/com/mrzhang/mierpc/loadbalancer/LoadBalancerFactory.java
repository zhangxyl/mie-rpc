package com.mrzhang.mierpc.loadbalancer;

import com.mrzhang.mierpc.serializer.JdkSerializer;
import com.mrzhang.mierpc.serializer.Serializer;
import com.mrzhang.mierpc.spi.SpiLoader;

/**
 * 序列化器工厂（用于获取序列化器对象）
 *

 */
public class LoadBalancerFactory {

    /**
     * 序列化映射（用于实现单例）
     */
    static {
        SpiLoader.load(Loadbalancer.class);
    }

    /**
     * 默认负载均衡器
     */
    private static final Loadbalancer DEFAULT_LOAD_BALANCER = new RamdomLoadBalancer();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Loadbalancer getInstance(String key) {
        return SpiLoader.getInstance(Loadbalancer.class,key);
    }

}

