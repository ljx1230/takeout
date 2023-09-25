package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.Result;

import java.util.List;

public interface DishService {
    void save(DishDTO dishDTO);

    Result page(DishPageQueryDTO dishPageQueryDTO);

    Result deleteBatch(List<Long> ids);

    void status(Integer status,Long id);


    Result info(Long id);

    Result update(DishDTO dishDTO);
}
