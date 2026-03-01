package com.dorm.manage.controller;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.common.Result;
import com.dorm.manage.entity.CheckIn;
import com.dorm.manage.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 入住登记接口
 *
 * @author dorm-manage
 */
@RestController
@RequestMapping("/checkin")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping("/submit")
    public Result<Void> submit(@RequestBody CheckIn checkIn, @RequestAttribute Long userId) {
        checkInService.submit(checkIn, userId);
        return Result.ok();
    }

    @GetMapping("/my/list")
    public Result<List<CheckIn>> myList(@RequestAttribute Long userId) {
        return Result.ok(checkInService.myList(userId));
    }

    @GetMapping("/admin/page")
    public Result<PageResult<CheckIn>> adminPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String college) {
        return Result.ok(checkInService.adminPage(pageNum, pageSize, status, college));
    }

    @PutMapping("/admin/audit")
    public Result<Void> audit(@RequestBody Map<String, Object> body, @RequestAttribute Long userId) {
        Long id = Long.valueOf(body.get("id").toString());
        boolean pass = Boolean.TRUE.equals(body.get("pass"));
        String rejectReason = body.get("rejectReason") != null ? body.get("rejectReason").toString() : null;
        Long bedId = body.get("bedId") != null ? Long.valueOf(body.get("bedId").toString()) : null;
        checkInService.audit(id, pass, rejectReason, userId, bedId);
        return Result.ok();
    }
}
