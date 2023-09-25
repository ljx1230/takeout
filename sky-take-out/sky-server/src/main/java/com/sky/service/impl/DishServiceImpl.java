package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private FlavorMapper flavorMapper;
    @Override
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        dishMapper.insertDish(dish);
        flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
        flavorMapper.insertFlavorList(flavors);
    }
}
