package com.dorm.manage.controller;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.common.Result;
import com.dorm.manage.entity.Dormitory;
import com.dorm.manage.service.DormitoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宿舍管理接口
 *
 * @author dorm-manage
 */
@RestController
@RequestMapping("/dormitory")
@RequiredArgsConstructor
public class DormitoryController {

    private final DormitoryService dormitoryService;

    /** 管理端：统计数据 */
    @GetMapping("/admin/stats")
    public Result<Map<String, Object>> stats() {
        return Result.ok(dormitoryService.getStats());
    }

    /** 管理端：分页列表 */
    @GetMapping("/admin/page")
    public Result<PageResult<Dormitory>> adminPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String buildingNo,
            @RequestParam(required = false) String roomNo,
            @RequestParam(required = false) Integer status) {
        return Result.ok(dormitoryService.page(pageNum, pageSize, buildingNo, roomNo, status));
    }

    /** 管理端：详情 */
    @GetMapping("/admin/detail/{id}")
    public Result<Dormitory> detail(@PathVariable Long id) {
        return Result.ok(dormitoryService.getById(id));
    }

    /** 管理端：新增 */
    @PostMapping("/admin/add")
    public Result<Void> add(@RequestBody Dormitory dormitory) {
        dormitoryService.add(dormitory);
        return Result.ok();
    }

    /** 管理端：编辑 */
    @PutMapping("/admin/update")
    public Result<Void> update(@RequestBody Dormitory dormitory) {
        dormitoryService.update(dormitory);
        return Result.ok();
    }

    /** 管理端：删除 */
    @DeleteMapping("/admin/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        dormitoryService.delete(id);
        return Result.ok();
    }

    /** 管理端：批量删除 */
    @DeleteMapping("/admin/batch-delete")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        dormitoryService.batchDelete(ids);
        return Result.ok();
    }
}