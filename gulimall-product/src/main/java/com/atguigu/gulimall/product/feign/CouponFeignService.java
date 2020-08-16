package com.atguigu.gulimall.product.feign;

import com.atguigu.common.to.SkuReductionTo;
import com.atguigu.common.to.SpuBoundsTo;
import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 优惠券远程接口
 *
 * @author itzhouq
 * @date 2020/8/16 20:13
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    /**
     * 保存spu的积分信息
     * @param spuBoundsTo
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    /**
     * 保存sku的优惠满减信息
     * @param skuReductionTo
     */
    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReductionTO(@RequestBody SkuReductionTo skuReductionTo);
}
