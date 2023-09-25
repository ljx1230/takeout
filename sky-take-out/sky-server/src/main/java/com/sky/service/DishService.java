package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.Result;

public interface DishService {
    void save(DishDTO dishDTO);

    Result page(DishPageQueryDTO dishPageQueryDTO);
}
