package com.mrzhang.mierpc.loadbalancer;

public interface LoadbalancerKeys {
    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    String RANDOM = "random";

    String CONSISTENT_HASH = "consistentHash";
}
