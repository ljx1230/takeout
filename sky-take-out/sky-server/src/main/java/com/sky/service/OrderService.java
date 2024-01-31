package com.sky.service;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * @Author: ljx
 * @Date: 2024/1/31 14:25
 */
public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersDTO);
}
