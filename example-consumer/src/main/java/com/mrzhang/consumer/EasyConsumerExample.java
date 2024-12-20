package com.mrzhang.consumer;

import com.mrzhang.common.model.User;
import com.mrzhang.common.service.UserService;
import com.mrzhang.mierpc.proxy.ServiceProxyFactory;

/**
 * 简易服务消费者示例
 *

 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        // 静态代理
        //UserService userService = null;
        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("mrzhang");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
