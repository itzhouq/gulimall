package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author itzhouq
 * @email itzhouq@163.com
 * @date 2020-07-19 15:58:47
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查出所有分类以及子分类，以树形结构组装起来
     */
    List<CategoryEntity> listWithTree();

    /**
     * 批量删除菜单
     * @param asList
     */
    void removeMenuByIds(List<Long> asList);

    /**
     * 找到catelogId的完整路径
     * @param catelogId  所属分类Id
     * @return [23, 45, 224]
     */
    Long[] findCatelogPath(Long catelogId);
}

