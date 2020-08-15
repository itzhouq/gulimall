package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * @author zhouquan
 * @date 2020/8/14 23:22
 */
@Data
public class AttrRespVo extends AttrVo {

    /**
     * 所属分类名字
     */
    private String catelogName;

    /**
     * 所属分组名字
     */
    private String groupName;

    /**
     * [2, 34, 225] //分类完整路径
     */
    private Long[] catelogPath;
}
