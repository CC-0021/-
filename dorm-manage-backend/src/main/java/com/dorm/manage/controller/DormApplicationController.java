package com.dorm.manage.controller;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.common.Result;
import com.dorm.manage.entity.DormApplication;
import com.dorm.manage.service.DormApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宿舍申请接口
 *
 * @author dorm-manage
 */
@RestController
@RequestMapping("/dorm-application")
@RequiredArgsConstructor
public class DormApplicationController {

    private final DormApplicationService dormApplicationService;

    @PostMapping("/submit")
    public Result<Void> submit(@RequestBody DormApplication application, @RequestAttribute Long userId) {
        dormApplicationService.submit(application, userId);
        return Result.ok();
    }

    @GetMapping("/my/list")
    public Result<List<DormApplication>> myList(@RequestAttribute Long userId) {
        return Result.ok(dormApplicationService.myList(userId));
    }

    @GetMapping("/admin/page")
    public Result<PageResult<DormApplication>> adminPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String applicationType) {
        return Result.ok(dormApplicationService.adminPage(pageNum, pageSize, status, applicationType));
    }

    @PutMapping("/admin/audit")
    public Result<Void> audit(@RequestBody Map<String, Object> body, @RequestAttribute Long userId) {
        Long id = Long.valueOf(body.get("id").toString());
        boolean pass = Boolean.TRUE.equals(body.get("pass"));
        String rejectReason = body.get("rejectReason") != null ? body.get("rejectReason").toString() : null;
        dormApplicationService.audit(id, pass, rejectReason, userId);
        return Result.ok();
    }
}
