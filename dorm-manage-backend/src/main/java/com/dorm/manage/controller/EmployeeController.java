package com.dorm.manage.controller;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.common.Result;
import com.dorm.manage.entity.Employee;
import com.dorm.manage.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工信息管理接口
 *
 * @author dorm-manage
 */
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /** 管理端：分页列表 */
    @GetMapping("/admin/page")
    public Result<PageResult<Employee>> adminPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Integer status) {
        return Result.ok(employeeService.page(pageNum, pageSize, name, department, status));
    }

    /** 管理端：详情 */
    @GetMapping("/admin/detail/{id}")
    public Result<Employee> detail(@PathVariable Long id) {
        return Result.ok(employeeService.getById(id));
    }

    /** 管理端：新增 */
    @PostMapping("/admin/add")
    public Result<Void> add(@RequestBody Employee employee) {
        employeeService.add(employee);
        return Result.ok();
    }

    /** 管理端：编辑 */
    @PutMapping("/admin/update")
    public Result<Void> update(@RequestBody Employee employee) {
        employeeService.update(employee);
        return Result.ok();
    }

    /** 管理端：删除 */
    @DeleteMapping("/admin/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return Result.ok();
    }

    /** 管理端：批量删除 */
    @DeleteMapping("/admin/batch-delete")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        employeeService.batchDelete(ids);
        return Result.ok();
    }
}