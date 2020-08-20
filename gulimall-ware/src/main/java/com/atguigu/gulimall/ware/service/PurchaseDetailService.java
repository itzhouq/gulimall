package com.atguigu.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author itzhouq
 * @email itzhouq@163.com
 * @date 2020-07-19 20:50:47
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据采购单Id查询采购需求的集合
     * @param purchaseId
     * @return
     */
    List<PurchaseDetailEntity> listDetailByPurchaseId(Long purchaseId);
}

