package com.atguigu.gulimall.ware.service.impl;

import com.atguigu.common.constant.WareConstant;
import com.atguigu.gulimall.ware.entity.PurchaseDetailEntity;
import com.atguigu.gulimall.ware.service.PurchaseDetailService;
import com.atguigu.gulimall.ware.service.WareSkuService;
import com.atguigu.gulimall.ware.vo.MergeVo;
import com.atguigu.gulimall.ware.vo.PurchaseDoneVo;
import com.atguigu.gulimall.ware.vo.PurchaseItemVo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.ware.dao.PurchaseDao;
import com.atguigu.gulimall.ware.entity.PurchaseEntity;
import com.atguigu.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;
import sun.tools.jstat.Literal;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Resource
    private PurchaseDetailService purchaseDetailService;

    @Resource
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(params), new QueryWrapper<>());

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status", 1)
        );
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = PurchaseEntity.builder().status(WareConstant.PurchaseEnum.CREATE.getCode()).createTime(new Date()).updateTime(new Date()).build();
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        // TODO 确认采购单状态为0或者1

        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntityList = items.stream().map(item -> PurchaseDetailEntity.builder().id(item).purchaseId(finalPurchaseId)
                        .status(WareConstant.PurchaseDetailEnum.ASSIGNED.getCode()).build()).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(purchaseDetailEntityList);

        this.updateById(PurchaseEntity.builder().id(purchaseId).updateTime(new Date()).build());
    }

    @Override
    public void received(List<Long> ids) {

        // 1. 确认当前采购单状态是新建或者已分配
        List<PurchaseEntity> purchaseEntityList = ids.stream().map(this::getById)
                .filter(item -> item.getStatus() == WareConstant.PurchaseEnum.CREATE.getCode() || item.getStatus() == WareConstant.PurchaseEnum.ASSIGNED.getCode())
                .peek(item -> item.setStatus(WareConstant.PurchaseEnum.RECEIVE.getCode()))
                .peek(item -> item.setUpdateTime(new Date()))
                .collect(Collectors.toList());

        // 2. 改变采购单的状态为已领取
        this.updateBatchById(purchaseEntityList);

        // 3. 改变采购需求为正在采购
        purchaseEntityList.forEach(purchaseEntity -> {
            List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailService.listDetailByPurchaseId(purchaseEntity.getId());
            List<PurchaseDetailEntity> collect = purchaseDetailEntityList.stream().map(purchaseDetailEntity -> PurchaseDetailEntity.builder()
                    .id(purchaseDetailEntity.getId()).status(WareConstant.PurchaseDetailEnum.BUYING.getCode()).build()).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(collect);
        });
    }

    @Transactional
    @Override
    public void finish(PurchaseDoneVo purchaseDoneVo) {
        Long id = purchaseDoneVo.getId();
        List<PurchaseItemVo> items = purchaseDoneVo.getItems();

        // 1. 改变采购需求的状态为成功或者失败
        boolean flag = true;
        List<PurchaseDetailEntity> updates = Lists.newArrayList();
        for (PurchaseItemVo item : items) {
            PurchaseDetailEntity purchaseDetailEntity = PurchaseDetailEntity.builder().build();
            if (item.getStatus() == WareConstant.PurchaseDetailEnum.HASHERROR.getCode()) {
                flag = false;
                purchaseDetailEntity.setStatus(item.getStatus());
            } else {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailEnum.FINISH.getCode());
                purchaseDetailEntity.setId(item.getItemId());
                updates.add(purchaseDetailEntity);
                // 3. 将成功的采购需求入库
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());

            }
        }
        purchaseDetailService.updateBatchById(updates);

        // 2. 改变采购单的状态为采购完成或者有异常
        PurchaseEntity purchaseEntity = PurchaseEntity.builder().id(id).updateTime(new Date())
                .status(flag ? WareConstant.PurchaseEnum.FINISH.getCode() : WareConstant.PurchaseEnum.HASHERROR.getCode()).build();
        this.updateById(purchaseEntity);




    }

}