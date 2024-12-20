package com.mrzhang.mierpc.loadbalancer;

import com.mrzhang.mierpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 */
public class RoundRobobinLoadBalancer implements Loadbalancer {
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        System.out.println("轮询负载均衡器");
        //只有一个服务 无须轮训
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        int i = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(i);

    }
}
