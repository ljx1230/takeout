package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public Result saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        setmealMapper.insert(setmeal);

        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertBatch(setmealDishes);

        return Result.success();
    }

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        List<SetmealVO> setmealVOList = setmealMapper.pageQuery(setmealPageQueryDTO);
        PageInfo<SetmealVO> pageInfo = new PageInfo<>(setmealVOList);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    @Transactional
    public void status(Integer status, Long id) {
        if(status == StatusConstant.ENABLE) {
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if(dishList == null && dishList.size() > 0) {
                dishList.forEach(dish -> {
                    if(StatusConstant.DISABLE == dish.getStatus()){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }

        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        List<Setmeal> setmealList = setmealMapper.getByIds(ids);
        // 判断套餐中是否启售
        for(Setmeal setmeal : setmealList) {
            if(setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        // 删除setmeal_dish数据
        setmealDishMapper.deleteBySetmealIds(ids);
        // 删除setmeal数据
        setmealMapper.deleteByIds(ids);
    }

    @Override
    public SetmealVO getInfoById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        ArrayList<Long> list = new ArrayList<>();
        list.add(id);
        List<Setmeal> setmealList = setmealMapper.getByIds(list);
        BeanUtils.copyProperties(setmealList.get(0),setmealVO);
        setmealVO.setSetmealDishes(setmealDishMapper.getBySetMealId(id));
        return setmealVO;
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);


        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        for(SetmealDish setmealDish : setmealDishList) {
            setmealDish.setSetmealId(setmealDTO.getId());
        }

        ArrayList<Long> idList = new ArrayList<>();
        setmealDishList.forEach(setmealDish -> idList.add(setmealDish.getSetmealId()));
        System.out.println(idList);
        setmealDishMapper.deleteBySetmealIds(idList);

        setmealDishMapper.insertBatch(setmealDishList);

    }
}
