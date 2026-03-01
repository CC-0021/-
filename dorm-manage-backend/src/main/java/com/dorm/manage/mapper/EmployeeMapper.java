package com.dorm.manage.mapper;

import com.dorm.manage.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工 Mapper
 *
 * @author dorm-manage
 */
@Mapper
public interface EmployeeMapper {

    List<Employee> selectPage(@Param("name") String name,
                               @Param("department") String department,
                               @Param("status") Integer status,
                               @Param("offset") long offset,
                               @Param("limit") int limit);

    long countPage(@Param("name") String name,
                   @Param("department") String department,
                   @Param("status") Integer status);

    Employee selectById(Long id);

    int insert(Employee employee);

    int updateById(Employee employee);

    int deleteById(Long id);
}
