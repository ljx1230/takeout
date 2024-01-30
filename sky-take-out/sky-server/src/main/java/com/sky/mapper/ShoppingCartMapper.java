package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: ljx
 * @Date: 2024/1/30 21:11
 */
@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> list(ShoppingCart shoppingCart);
    void updateNumberById(ShoppingCart shoppingCart);

    void insert(ShoppingCart shoppingCart);

    @Delete("delete from shopping_cart where user_id = #{id}")
    void deleteByUserId(Long id);

    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);
}
