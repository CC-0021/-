package com.dorm.manage.controller;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.common.Result;
import com.dorm.manage.entity.Violation;
import com.dorm.manage.service.ViolationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宿舍违规管理接口
 *
 * @author dorm-manage
 */
@RestController
@RequestMapping("/violation")
@RequiredArgsConstructor
public class ViolationController {

    private final ViolationService violationService;

    /** 管理端：分页列表 */
    @GetMapping("/admin/page")
    public Result<PageResult<Violation>> adminPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String roomNo) {
        return Result.ok(violationService.page(pageNum, pageSize, studentId, roomNo));
    }

    /** 管理端：详情 */
    @GetMapping("/admin/detail/{id}")
    public Result<Violation> detail(@PathVariable Long id) {
        return Result.ok(violationService.getById(id));
    }

    /** 管理端：新增 */
    @PostMapping("/admin/add")
    public Result<Void> add(@RequestBody Violation violation) {
        violationService.add(violation);
        return Result.ok();
    }

    /** 管理端：编辑 */
    @PutMapping("/admin/update")
    public Result<Void> update(@RequestBody Violation violation) {
        violationService.update(violation);
        return Result.ok();
    }

    /** 管理端：删除 */
    @DeleteMapping("/admin/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        violationService.delete(id);
        return Result.ok();
    }

    /** 管理端：批量删除 */
    @DeleteMapping("/admin/batch-delete")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        violationService.batchDelete(ids);
        return Result.ok();
    }
}
