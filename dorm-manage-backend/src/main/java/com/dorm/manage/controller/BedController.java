package com.dorm.manage.controller;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.common.Result;
import com.dorm.manage.entity.Bed;
import com.dorm.manage.entity.Dormitory;
import com.dorm.manage.entity.User;
import com.dorm.manage.mapper.UserMapper;
import com.dorm.manage.service.BedService;
import com.dorm.manage.service.DormitoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final DormitoryService dormitoryService;
    private final UserMapper userMapper;

    /** 管理端：分页列表 */
    @GetMapping("/admin/page")
    public Result<PageResult<Bed>> adminPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long dormitoryId,
            @RequestParam(required = false) String bedNo,
            @RequestParam(required = false) Integer status) {
        return Result.ok(bedService.page(pageNum, pageSize, dormitoryId, bedNo, status));
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
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status, @RequestParam(required = false) String studentId) {
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

    /** 学生端：获取我的宿舍信息 */
    @GetMapping("/my/dormitory")
    public Result<Map<String, Object>> myDormitory(@RequestAttribute Long userId) {
        Map<String, Object> result = new HashMap<>();

        // 1. 查询当前用户信息获取学号
        User currentUser = userMapper.selectById(userId);
        if (currentUser == null) {
            return Result.ok(result);
        }
        String studentNo = currentUser.getUsername();

        // 2. 查询当前学生分配的床位（用学号查询）
        Bed myBed = bedService.getByStudentId(studentNo);
        if (myBed == null) {
            return Result.ok(result);
        }
        result.put("myBed", myBed);

        // 3. 查询宿舍信息（通过房间号查询）
        Dormitory dormitory = dormitoryService.getByRoomNo(myBed.getDormitoryId());
        result.put("dormitory", dormitory);

        // 4. 查询同宿舍的所有床位及学生信息
        List<Bed> beds = bedService.getByDormitoryId(myBed.getDormitoryId());
        List<Map<String, Object>> roommates = new ArrayList<>();
        for (Bed bed : beds) {
            Map<String, Object> mate = new HashMap<>();
            mate.put("bedId", bed.getId());
            mate.put("bedNo", bed.getBedNo());
            mate.put("status", bed.getStatus());
            if (bed.getStudentId() != null) {
                User student = userMapper.selectByUsername(bed.getStudentId());
                if (student != null) {
                    mate.put("studentId", student.getId());
                    mate.put("studentName", student.getRealName());
                    mate.put("studentNo", student.getUsername());
                }
            }
            roommates.add(mate);
        }
        result.put("roommates", roommates);

        return Result.ok(result);
    }
}