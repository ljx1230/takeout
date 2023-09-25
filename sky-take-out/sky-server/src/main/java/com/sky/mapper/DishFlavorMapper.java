package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getByDishId(Long id);

    void insertFlavorList(@Param("flavors") List<DishFlavor> flavors);

    void deleteByDishIds(@Param("ids") List<Long> ids);

    void deleteByids(@Param("list") List<DishFlavor> dishFlavorList);
}
