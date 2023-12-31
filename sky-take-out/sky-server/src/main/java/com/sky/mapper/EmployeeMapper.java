package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @AutoFill(OperationType.INSERT)
    @Insert("insert into employee" +
            " values(null,#{name},#{username}," +
            "#{password},#{phone},#{sex},#{idNumber},#{status}," +
            "#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Employee employee);

    List<Employee> getByName(String name);

    int updateStatusById(@Param("status") Integer status, @Param("id") Integer id);

    @Select("select * from employee where id = #{id}")
    Employee getById(Integer id);

    @AutoFill(OperationType.UPDATE)
    int update(Employee employee);

    void updatePasswordByid(Employee employee);
}
