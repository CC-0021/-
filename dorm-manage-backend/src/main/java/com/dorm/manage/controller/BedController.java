package com.dorm.manage.controller;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.common.Result;
import com.dorm.manage.entity.Bed;
import com.dorm.manage.service.BedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 床位管理接口
 *
 * @author dorm-manage
 */
@RestController
@RequestMapping("/bed")
@RequiredArgsConstructor
public class BedController {

    private final BedService bedService;

    /** 管理端：分页列表 */
    @GetMapping("/admin/page")
    public Result<PageResult<Bed>> adminPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long dormitoryId,
            @RequestParam(required = false) Integer status) {
        return Result.ok(bedService.page(pageNum, pageSize, dormitoryId, status));
    }

    /** 管理端：根据宿舍ID获取床位列表 */
    @GetMapping("/admin/list-by-dorm/{dormitoryId}")
    public Result<List<Bed>> listByDormitory(@PathVariable Long dormitoryId) {
        return Result.ok(bedService.getByDormitoryId(dormitoryId));
    }

    /** 管理端：详情 */
    @GetMapping("/admin/detail/{id}")
    public Result<Bed> detail(@PathVariable Long id) {
        return Result.ok(bedService.getById(id));
    }

    /** 管理端：新增 */
    @PostMapping("/admin/add")
    public Result<Void> add(@RequestBody Bed bed) {
        bedService.add(bed);
        return Result.ok();
    }

    /** 管理端：编辑 */
    @PutMapping("/admin/update")
    public Result<Void> update(@RequestBody Bed bed) {
        bedService.update(bed);
        return Result.ok();
    }

    /** 管理端：更新状态 */
    @PutMapping("/admin/update-status")
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status, @RequestParam(required = false) Long studentId) {
        bedService.updateStatus(id, status, studentId);
        return Result.ok();
    }

    /** 管理端：删除 */
    @DeleteMapping("/admin/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bedService.delete(id);
        return Result.ok();
    }

    /** 管理端：批量删除 */
    @DeleteMapping("/admin/batch-delete")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        bedService.batchDelete(ids);
        return Result.ok();
    }
}