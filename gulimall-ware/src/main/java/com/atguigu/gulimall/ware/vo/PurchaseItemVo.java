package com.atguigu.gulimall.ware.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhouquan
 * @date 2020/8/21 22:25
 */
@Data
@Builder
public class PurchaseItemVo {

    /**
     * 采购需求ID
     */
    private Long itemId;

    /**
     * 采购需求的状态
     */
    private Integer status;

    /**
     * 采购备注
     */
    private String reason;
}
