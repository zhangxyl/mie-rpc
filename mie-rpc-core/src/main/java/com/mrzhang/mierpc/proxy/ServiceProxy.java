package com.mrzhang.mierpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.mrzhang.mierpc.RpcApplication;
import com.mrzhang.mierpc.config.RpcConfig;
import com.mrzhang.mierpc.constant.RpcConstant;
import com.mrzhang.mierpc.fault.retry.RetryStrategy;
import com.mrzhang.mierpc.fault.retry.RetryStrategyFactory;
import com.mrzhang.mierpc.fault.tolerant.TolerantStrategy;
import com.mrzhang.mierpc.fault.tolerant.TolerantStrategyFactory;
import com.mrzhang.mierpc.loadbalancer.LoadBalancerFactory;
import com.mrzhang.mierpc.loadbalancer.Loadbalancer;
import com.mrzhang.mierpc.model.RpcRequest;
import com.mrzhang.mierpc.model.RpcResponse;
import com.mrzhang.mierpc.model.ServiceMetaInfo;
import com.mrzhang.mierpc.registry.Registry;
import com.mrzhang.mierpc.registry.RegistryFactory;
import com.mrzhang.mierpc.serializer.JdkSerializer;
import com.mrzhang.mierpc.serializer.Serializer;
import com.mrzhang.mierpc.serializer.SerializerFactory;
import com.mrzhang.mierpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * 服务代理（JDK 动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        String serviceName = method.getDeclaringClass().getName();

            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            //负载均衡
            String loadBalancer = rpcConfig.getLoadBalancer();
            Loadbalancer loadbalancer = LoadBalancerFactory.getInstance(loadBalancer);
            HashMap<String, Object> requsetParam = new HashMap<>();
            requsetParam.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo metaInfo = loadbalancer.select(requsetParam, serviceMetaInfoList);
            //ServiceMetaInfo serviceMetaInfo1 = serviceMetaInfoList.get(0);
//        try {
//            // 序列化
//            byte[] bodyBytes = serializer.serialize(rpcRequest);
//            // 发送请求
//            // todo 注意，这里地址被硬编码了（需要使用注册中心和服务发现机制解决）
//            String serviceAddress = serviceMetaInfo1.getServiceAddress();
//            System.out.println("提供者地址:"+serviceAddress);
//            try (HttpResponse httpResponse = HttpRequest.post(serviceAddress)
//                    .body(bodyBytes)
//                    .execute()) {
//                byte[] result = httpResponse.bodyBytes();
//                // 反序列化
//                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//                return rpcResponse.getData();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        RpcResponse rpcResponse;
        try {
            //tcp请求
            //RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, metaInfo);
            // 使用重试机制
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, metaInfo)
            );
        } catch (Exception e) {
            //容错机制
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
             rpcResponse = tolerantStrategy.doTolerant(null, e);
        }
        return rpcResponse.getData();
    }
}
