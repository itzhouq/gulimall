package com.atguigu.gulimall.ware.vo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhouquan
 * @date 2020/8/21 22:24
 */
@Data
@Builder
public class PurchaseDoneVo {

    /**
     * 整单Id
     */
    @NotNull
    private Long id;

    /**
     * 采购需求列表
     */
    private List<PurchaseItemVo> items;
}
