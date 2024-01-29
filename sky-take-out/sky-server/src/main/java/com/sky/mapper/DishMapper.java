package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    int insertDish(Dish dish);

    List<DishVO> getDishVoList(Dish dish);

    List<Dish> getListByIds(@Param("ids") List<Long> ids);

    void deleteByids(@Param("ids") List<Long> ids);

    void updateStatusById(@Param("status") Integer status, @Param("id") Long id);

    Dish getDishInfoById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    List<Dish> getDIshListByCategoryId(Long categoryId);

    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long id);

    List<Dish> list(Dish dish);

}
