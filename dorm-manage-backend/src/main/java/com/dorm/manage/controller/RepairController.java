package com.dorm.manage.controller;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.common.Result;
import com.dorm.manage.entity.Repair;
import com.dorm.manage.service.RepairService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 报修接口 - 学生端提交/记录/评价 + 管理端处理/统计
 *
 * @author dorm-manage
 */
@RestController
@RequestMapping("/repair")
@RequiredArgsConstructor
public class RepairController {

    private final RepairService repairService;

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    /** 学生端：提交报修 */
    @PostMapping("/submit")
    public Result<Void> submit(@RequestBody Repair repair, @RequestAttribute Long userId, HttpServletRequest request) {
        repairService.submit(repair, userId, getClientIp(request));
        return Result.ok();
    }

    /** 学生端：撤销（仅待处理） */
    @PutMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Long id, @RequestAttribute Long userId) {
        repairService.cancel(id, userId);
        return Result.ok();
    }

    /** 学生端：我的报修记录（分页、筛选、导出用同一列表数据） */
    @GetMapping("/my/list")
    public Result<PageResult<Repair>> myList(
            @RequestAttribute Long userId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String repairType,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return Result.ok(repairService.myList(userId, pageNum, pageSize, status, repairType, startTime, endTime));
    }

    /** 学生端：报修详情 */
    @GetMapping("/detail/{id}")
    public Result<Repair> detail(@PathVariable Long id) {
        return Result.ok(repairService.getById(id));
    }

    /** 学生端：评价（仅已完成） */
    @PostMapping("/evaluate")
    public Result<Void> evaluate(@RequestBody Map<String, Object> body, @RequestAttribute Long userId) {
        Long repairId = Long.valueOf(body.get("repairId").toString());
        Integer score = Integer.valueOf(body.get("score").toString());
        String content = body.get("content") != null ? body.get("content").toString() : null;
        repairService.evaluate(repairId, userId, score, content);
        return Result.ok();
    }

    /** 管理端：分页列表 */
    @GetMapping("/admin/page")
    public Result<PageResult<Repair>> adminPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String repairType,
            @RequestParam(required = false) String buildingNo,
            @RequestParam(required = false) Long handlerId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return Result.ok(repairService.adminPage(pageNum, pageSize, status, repairType, buildingNo, handlerId, startTime, endTime));
    }

    /** 管理端：分配处理人 */
    @PutMapping("/admin/assign")
    public Result<Void> assign(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        Long handlerId = Long.valueOf(body.get("handlerId").toString());
        String handlerName = body.get("handlerName") != null ? body.get("handlerName").toString() : "";
        repairService.assignHandler(id, handlerId, handlerName);
        return Result.ok();
    }

    /** 管理端：处理/完成 */
    @PutMapping("/admin/handle")
    public Result<Void> handle(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        String handleRemark = body.get("handleRemark") != null ? body.get("handleRemark").toString() : null;
        String handleImages = body.get("handleImages") != null ? body.get("handleImages").toString() : null;
        boolean complete = Boolean.TRUE.equals(body.get("complete"));
        repairService.handle(id, handleRemark, handleImages, complete);
        return Result.ok();
    }

    /** 管理端：按类型统计 */
    @GetMapping("/admin/stats/type")
    public Result<List<Map<String, Object>>> statsByType(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return Result.ok(repairService.statsByType(startTime, endTime));
    }

    /** 管理端：按楼栋统计 */
    @GetMapping("/admin/stats/building")
    public Result<List<Map<String, Object>>> statsByBuilding(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return Result.ok(repairService.statsByBuilding(startTime, endTime));
    }
}
