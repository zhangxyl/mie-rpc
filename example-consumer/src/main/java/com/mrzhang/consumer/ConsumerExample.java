package com.mrzhang.consumer;

import com.mrzhang.mierpc.config.RpcConfig;
import com.mrzhang.mierpc.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
