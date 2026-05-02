package com.dorm.manage.service;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.entity.Employee;
import com.dorm.manage.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 员工信息管理服务
 *
 * @author dorm-manage
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeMapper employeeMapper;

    /**
     * 分页查询员工列表
     */
    public PageResult<Employee> page(int pageNum, int pageSize, String name, String department, Integer status) {
        long total = employeeMapper.countPage(name, department, status);
        int offset = (pageNum - 1) * pageSize;
        List<Employee> list = employeeMapper.selectPage(name, department, status, offset, pageSize);
        return new PageResult<>(total, list);
    }

    /**
     * 根据ID获取员工信息
     */
    public Employee getById(Long id) {
        return employeeMapper.selectById(id);
    }

    /**
     * 新增员工
     */
    public void add(Employee employee) {
        employeeMapper.insert(employee);
    }

    /**
     * 更新员工信息
     */
    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    /**
     * 删除员工
     */
    public void delete(Long id) {
        employeeMapper.deleteById(id);
    }

    /**
     * 批量删除员工
     */
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            employeeMapper.deleteById(id);
        }
    }
}