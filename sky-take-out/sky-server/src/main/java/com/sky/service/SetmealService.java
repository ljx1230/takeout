package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;

public interface SetmealService {
    Result saveWithDish(SetmealDTO setmealDTO);
}
