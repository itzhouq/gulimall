package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.vo.SpuSaveVo;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.SpuInfoDao;
import com.atguigu.gulimall.product.entity.SpuInfoEntity;
import com.atguigu.gulimall.product.service.SpuInfoService;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        // 1. 保存spu的基本信息: pms_spu_info


        // 2. 保存spu的描述图片: pms_spu_info_desc


        // 3. 保存spu的图片集: pms_spu_images


        // 4. 保存spu的规格参数: pms_product_attr_value

        // 5. 保存spu的积分信息: gulimall_sms -> sms_spu_bounds


        // 6. 保存当前spu对应的所有sku信息
        // 6.1 sku的基本信息: pms_sku_info
        // 6.2 sku的图片信息: pms_sku_images
        // 6.3 sku的销售属性信息: pms_sku_sale_attr_value
        // 6.4 sku的优惠满减等信息: gulimall_sms -> sms_sku_ladder\sms_sku_full_reduction\sms_
    }

}