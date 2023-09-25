package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlavorMapper {

    void insertFlavorList(@Param("flavors") List<DishFlavor> flavors);
}
