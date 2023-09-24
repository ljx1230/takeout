package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    Result<String> addEmployee(EmployeeDTO employee,String token);

    Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO);

    Result status(Integer status, Integer id);

    Result getEmployeeById(Integer id);

    Result updateEmployeeInfo(EmployeeDTO employeeDTO);

    Result editPassword(PasswordEditDTO passwordEditDTO);
}
