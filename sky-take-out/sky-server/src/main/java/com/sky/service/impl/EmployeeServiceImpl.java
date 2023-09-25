package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对
        password = DigestUtils .md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public Result<String> addEmployee(EmployeeDTO employee1,String token) {
        Employee employee = new Employee();

        BeanUtils.copyProperties(employee1,employee);

        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(),token);
        Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeMapper.insert(employee);
        return Result.success(null);
    }

    @Override
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        List<Employee> employeeList = employeeMapper.getByName(employeePageQueryDTO.getName());
        PageInfo<Employee> pageInfo = new PageInfo<>(employeeList);
        PageResult pageResult = new PageResult();
        List<Employee> list = pageInfo.getList();
        for(Employee employee : list) {
            employee.setPassword("");
        }
        pageResult.setRecords(list);
        pageResult.setTotal(pageInfo.getTotal());
        return Result.success(pageResult);
    }

    @Override
    public Result status(Integer status, Integer id) {
        int rows = employeeMapper.updateStatusById(status,id);
        if(rows == 0) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return Result.success(null);
    }

    @Override
    public Result getEmployeeById(Integer id) {
        Employee employee = employeeMapper.getById(id);
        if(employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        employee.setPassword("");
        return Result.success(employee);
    }

    @Override
    public Result updateEmployeeInfo(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        int rows = employeeMapper.update(employee);
        if(rows == 0) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return Result.success(null);
    }

    @Override
    public Result editPassword(PasswordEditDTO passwordEditDTO) {
        Employee employee = employeeMapper.getById(BaseContext.getCurrentId().intValue());
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes());
        if(md5DigestAsHex.equals(employee.getPassword())) {
            employee.setPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
            employeeMapper.updatePasswordByid(employee);
            return Result.success();
        }
        return Result.error(MessageConstant.PASSWORD_EDIT_FAILED);
    }

}
