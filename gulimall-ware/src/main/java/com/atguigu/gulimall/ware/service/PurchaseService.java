package com.atguigu.gulimall.ware.service;

import com.atguigu.gulimall.ware.vo.MergeVo;
import com.atguigu.gulimall.ware.vo.PurchaseDoneVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author itzhouq
 * @email itzhouq@163.com
 * @date 2020-07-19 20:50:47
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询未领取的采购单
     * @param params
     * @return
     */
    PageUtils queryPageUnreceive(Map<String, Object> params);

    /**
     * 合并采购需求
     * @param mergeVo
     */
    void mergePurchase(MergeVo mergeVo);

    /**
     * 领取采购单
     * @param ids 采购单Id集合
     */
    void received(List<Long> ids);

    /**
     * 合并采购需求
     * @param purchaseDoneVo
     */
    void finish(PurchaseDoneVo purchaseDoneVo);
}

