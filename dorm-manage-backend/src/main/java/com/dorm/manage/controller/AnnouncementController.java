package com.dorm.manage.controller;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.common.Result;
import com.dorm.manage.entity.Announcement;
import com.dorm.manage.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 公告接口 - 学生端列表/详情 + 管理端CRUD
 *
 * @author dorm-manage
 */
@RestController
@RequestMapping("/announcement")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    /** 学生端：分页列表（按发布时间降序、标题模糊） */
    @GetMapping("/list")
    public Result<PageResult<Announcement>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String title) {
        return Result.ok(announcementService.listWithCache(pageNum, pageSize, title));
    }

    /** 学生端：详情 */
    @GetMapping("/detail/{id}")
    public Result<Announcement> detail(@PathVariable Long id) {
        return Result.ok(announcementService.getById(id));
    }

    /** 管理端：分页 */
    @GetMapping("/admin/page")
    public Result<PageResult<Announcement>> adminPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status) {
        return Result.ok(announcementService.adminPage(pageNum, pageSize, title, status));
    }

    /** 管理端：新增 */
    @PostMapping("/admin/add")
    public Result<Void> add(@RequestBody Announcement announcement) {
        announcementService.add(announcement);
        return Result.ok();
    }

    /** 管理端：编辑 */
    @PutMapping("/admin/update")
    public Result<Void> update(@RequestBody Announcement announcement) {
        announcementService.update(announcement);
        return Result.ok();
    }

    /** 管理端：删除（仅草稿） */
    @DeleteMapping("/admin/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return Result.ok();
    }
}
