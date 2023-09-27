package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    List<Long> getSetmealIdsByDishIds(@Param("dishIds") List<Long> dishIds);

    void insertBatch(List<SetmealDish> setmealDishes);

    void deleteBySetmealIds(List<Long> ids);
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetMealId(Long id);
}
