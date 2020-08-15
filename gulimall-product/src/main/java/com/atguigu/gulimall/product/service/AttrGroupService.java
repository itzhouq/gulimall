package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.AttrGroupRelationVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;

import java.util.Map;

/**
 * 属性分组
 *
 * @author itzhouq
 * @email itzhouq@163.com
 * @date 2020-07-19 15:58:47
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    /**
     * 删除属性与分组的关联关系
     * @param groupRelationVos
     */
    void deleteRelation(AttrGroupRelationVo[] groupRelationVos);
}

