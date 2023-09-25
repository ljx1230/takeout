package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}",dishDTO);
        dishService.save(dishDTO);
        return Result.success();
    }
    @GetMapping("page")
    @ApiOperation("菜品分页查询")
    public Result page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询:{}",dishPageQueryDTO);
        Result result = dishService.page(dishPageQueryDTO);
        return result;
    }

    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("菜品批量删除:{}",ids);
        Result result = dishService.deleteBatch(ids);
        return result;
    }

    @PostMapping("status/{status}")
    @ApiOperation("菜品起售/停售")
    public Result status(@PathVariable Integer status,Long id) {
        log.info("菜品起售/停售:{}",status,id);
        dishService.status(status,id);
        return Result.success();
    }
}
