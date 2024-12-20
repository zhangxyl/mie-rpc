package com.mrzhang.mierpcspringbootstarter.annotation;

import com.mrzhang.mierpcspringbootstarter.bootstrap.RpcConsumerBootstrap;
import com.mrzhang.mierpcspringbootstarter.bootstrap.RpcInitBootstrap;
import com.mrzhang.mierpcspringbootstarter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 Rpc 注解
 *


 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    /**
     * 需要启动 server
     *
     * @return
     */
    boolean needServer() default true;
}
