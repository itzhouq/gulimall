package com.atguigu.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author itzhouq
 * @email itzhouq@163.com
 * @date 2020-07-19 20:50:47
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 添加库存
     * @param skuId  商品ID
     * @param wareId 仓库ID
     * @param skuNum 新加库存数量
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);
}

