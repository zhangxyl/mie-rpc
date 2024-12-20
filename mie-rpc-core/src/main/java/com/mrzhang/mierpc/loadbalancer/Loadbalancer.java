package com.mrzhang.mierpc.loadbalancer;

import com.mrzhang.mierpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

public interface Loadbalancer {
    /**
     *
     * @param requestParams 请求参数
     * @param serviceMetaInfoList 可用服务列表
     * @return
     */
    ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
