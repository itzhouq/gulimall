package com.atguigu.gulimall.product.vo;

import com.atguigu.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * 分组和关联属性
 *
 * @author itzhouq
 * @date 2020/8/15 21:08
 */
@Data
public class AttrGroupWithAttrsVo {
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 所有关联属性
     */
    private List<AttrEntity> attrs;
}
