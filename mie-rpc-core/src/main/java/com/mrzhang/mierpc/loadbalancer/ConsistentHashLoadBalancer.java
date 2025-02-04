package com.mrzhang.mierpc.loadbalancer;

import com.mrzhang.mierpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡器
 */

public class ConsistentHashLoadBalancer implements Loadbalancer {
    /**
     * 一致性 Hash 环，存放虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        System.out.println("一致性哈希负载均衡器");
        //构建虚拟节点环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }
        //获取调用请求的hash
        int hash = getHash(requestParams);
        //选择接近或大于等于调用请求hash值的虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> integerServiceMetaInfoEntry = virtualNodes.ceilingEntry(hash);
        if (integerServiceMetaInfoEntry == null) {
            integerServiceMetaInfoEntry = virtualNodes.firstEntry();
        }
        return integerServiceMetaInfoEntry.getValue();
    }

    private int getHash(Object s) {
        return s.hashCode();
    }
}
