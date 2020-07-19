package com.atguigu.gulimall.order.dao;

import com.atguigu.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author itzhouq
 * @email itzhouq@163.com
 * @date 2020-07-19 20:33:42
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
