package com.mrzhang.provider;

import com.mrzhang.common.service.UserService;
import com.mrzhang.mierpc.RpcApplication;
import com.mrzhang.mierpc.config.RpcConfig;
import com.mrzhang.mierpc.model.ServiceMetaInfo;
import com.mrzhang.mierpc.registry.LocalRegistry;
import com.mrzhang.mierpc.registry.Registry;
import com.mrzhang.mierpc.registry.RegistryFactory;
import com.mrzhang.mierpc.server.HttpServer;
import com.mrzhang.mierpc.server.VertxHttpServer;
import com.mrzhang.mierpc.server.tcp.VertxTcpServer;


/**
 * 简易服务提供者示例
 *
 */
public class EasyProviderExample {

    public static void main(String[] args) throws Exception {
        RpcApplication.init();
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(UserService.class.getName());
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

        String registry1 = rpcConfig.getRegistryConfig().getRegistry();
        Registry registry = RegistryFactory.getInstance(registry1);
        registry.register(serviceMetaInfo);
        // 启动 web 服务
       // HttpServer httpServer = new VertxHttpServer();
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
