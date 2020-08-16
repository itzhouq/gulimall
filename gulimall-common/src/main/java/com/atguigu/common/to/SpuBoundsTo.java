package com.atguigu.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 优惠券传输对象
 * @author itzhouq
 * @date 2020/8/16 20:20
 */
@Data
public class SpuBoundsTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
