package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private FlavorMapper flavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Override
    @Transactional
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        dishMapper.insertDish(dish);
        flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
        flavorMapper.insertFlavorList(flavors);
    }

    @Override
    public Result page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishPageQueryDTO,dish);
        if(dishPageQueryDTO.getCategoryId() != null)
            dish.setCategoryId(dishPageQueryDTO.getCategoryId().longValue());
        List<DishVO> dishVOList = dishMapper.getDishVoList(dish);

        PageInfo<DishVO> pageInfo = new PageInfo<>(dishVOList);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRecords(pageInfo.getList());
        return Result.success(pageResult);
    }

    @Override
    @Transactional
    public Result deleteBatch(List<Long> ids) {
        List<Dish> dishList = dishMapper.getListByIds(ids);

        // 判断当前菜品中是否存在启售中的商品
        for(Dish dish : dishList) {
            System.out.println(dish);
            if(dish.getStatus().equals(StatusConstant.ENABLE))
                return Result.error(MessageConstant.DISH_ON_SALE);
        }
        // 判断当前菜品是否被套餐关联了
        List<Long> setmealIdsByDishIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIdsByDishIds != null && !setmealIdsByDishIds.isEmpty()) {
            return Result.error(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除菜品表的数据
        dishMapper.deleteByids(ids);
        // 删除口味表中的数据
        flavorMapper.deleteByDishIds(ids);
        return Result.success();
    }

    @Override
    public void status(Integer status,Long id) {
        dishMapper.updateStatusById(status,id);
    }
}
