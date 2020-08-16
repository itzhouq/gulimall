package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author itzhouq
 * @email itzhouq@163.com
 * @date 2020-07-19 15:58:46
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存sku的基本信息
     * @param skuInfoEntity
     */
    void saveSkuInfo(SkuInfoEntity skuInfoEntity);
}

