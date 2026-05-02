package com.dorm.manage.controller;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.common.Result;
import com.dorm.manage.entity.Visitor;
import com.dorm.manage.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 来访登记接口
 *
 * @author dorm-manage
 */
@RestController
@RequestMapping("/visitor")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    /** 管理端：分页列表 */
    @GetMapping("/admin/page")
    public Result<PageResult<Visitor>> adminPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String roomNo,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        LocalDateTime start = parseDateTime(startTime);
        LocalDateTime end = parseDateTime(endTime);
        return Result.ok(visitorService.page(pageNum, pageSize, roomNo, start, end));
    }

    /** 管理端：详情 */
    @GetMapping("/admin/detail/{id}")
    public Result<Visitor> detail(@PathVariable Long id) {
        return Result.ok(visitorService.getById(id));
    }

    /** 管理端：新增 */
    @PostMapping("/admin/add")
    public Result<Void> add(@RequestBody Visitor visitor) {
        visitorService.add(visitor);
        return Result.ok();
    }

    /** 管理端：编辑 */
    @PutMapping("/admin/update")
    public Result<Void> update(@RequestBody Visitor visitor) {
        visitorService.update(visitor);
        return Result.ok();
    }

    /** 管理端：按天统计 */
    @GetMapping("/admin/stats/day")
    public Result<List<Map<String, Object>>> statsByDay(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return Result.ok(visitorService.countByDay(parseDateTime(startTime), parseDateTime(endTime)));
    }

    /** 管理端：按楼栋统计 */
    @GetMapping("/admin/stats/building")
    public Result<List<Map<String, Object>>> statsByBuilding(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return Result.ok(visitorService.countByBuilding(parseDateTime(startTime), parseDateTime(endTime)));
    }
}