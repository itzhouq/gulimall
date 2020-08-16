package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author itzhouq
 * @email itzhouq@163.com
 * @date 2020-07-19 15:58:46
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存spu的描述图片
     * @param descEntity
     */
    void saveSpuInfoDesc(SpuInfoDescEntity descEntity);
}

