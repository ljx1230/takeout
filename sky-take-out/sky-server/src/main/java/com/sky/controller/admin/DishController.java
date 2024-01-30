package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}",dishDTO);
        dishService.save(dishDTO);

        String key = "dish_" + dishDTO.getCategoryId();
        redisTemplate.delete(key);
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

        this.clearCache("dish_*");

        return result;
    }

    @PostMapping("status/{status}")
    @ApiOperation("菜品起售/停售")
    public Result status(@PathVariable Integer status,Long id) {
        log.info("菜品起售/停售:{}",status,id);
        dishService.status(status,id);

        this.clearCache("dish_*");

        return Result.success();
    }

    @GetMapping("{id}")
    @ApiOperation("根据id查询菜品")
    public Result info(@PathVariable Long id) {
        log.info("根据id查询菜品",id);
        Result result = dishService.info(id);
        return result;
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        Result result = dishService.update(dishDTO);
        this.clearCache("dish_*");
        return result;
    }

    @GetMapping("list")
    @ApiOperation("根据分类id查询菜品")
    public Result list(Long categoryId) {
        Result result = dishService.list(categoryId);
        return result;
    }

    // 清理缓存数据
    private void clearCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
