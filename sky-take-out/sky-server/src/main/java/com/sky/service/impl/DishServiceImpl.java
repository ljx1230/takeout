package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
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
}
