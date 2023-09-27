package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    Result saveWithDish(SetmealDTO setmealDTO);

    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    void status(Integer status, Long id);

    void deleteBatch(List<Long> ids);

    SetmealVO getInfoById(Long id);

    void update(SetmealDTO setmealDTO);
}
