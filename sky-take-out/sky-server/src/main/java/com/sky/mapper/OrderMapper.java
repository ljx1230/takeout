package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: ljx
 * @Date: 2024/1/31 14:29
 */
@Mapper
public interface OrderMapper {
    void insert(Orders orders);
}
