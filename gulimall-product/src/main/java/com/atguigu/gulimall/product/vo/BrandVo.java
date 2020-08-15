package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * 品牌VO
 * @author itzhouq
 * @date 2020/8/15 20:30
 */

@Data
public class BrandVo {

    /**
     * 分类Id
     */
    private Long catId;

    /**
     * 品牌名称
     */
    private String brandName;
}
