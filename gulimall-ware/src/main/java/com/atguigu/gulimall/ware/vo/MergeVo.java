package com.atguigu.gulimall.ware.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author zhouquan
 * @date 2020/8/20 21:32
 */
@Data
@Builder
public class MergeVo {

    /**
     * 整单Id
     */
    private Long purchaseId;

    /**
     * 合并项集合
     */
    private List<Long> items;
}
