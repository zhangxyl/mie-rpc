package com.mrzhang.springbootprovider;

import com.mrzhang.common.model.User;
import com.mrzhang.common.service.UserService;
import com.mrzhang.mierpcspringbootstarter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 *

 */
@Service
@RpcService
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
