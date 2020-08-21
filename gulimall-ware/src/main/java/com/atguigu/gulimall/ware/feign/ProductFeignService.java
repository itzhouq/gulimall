package com.atguigu.gulimall.ware.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author itzhouq
 * @date 2020/8/21 23:15
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    /*
     * 1. 让所有请求过网关
     *         @FeignClient("gulimall-gateway") 给gulimall-gateway所在的机器发请求
     *          /api/product/skuinfo/info/{skuId}
     *  2. 直接让后台指定服务器处理
     *         @FeignClient("gulimall-product")
     *          /product/skuinfo/info/{skuId}
     */

    /**
     * 信息
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);
}
