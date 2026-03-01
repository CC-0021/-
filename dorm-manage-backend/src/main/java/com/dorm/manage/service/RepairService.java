package com.dorm.manage.service;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.entity.Repair;
import com.dorm.manage.entity.RepairEvaluation;
import com.dorm.manage.exception.BusinessException;
import com.dorm.manage.mapper.RepairMapper;
import com.dorm.manage.mapper.RepairEvaluationMapper;
import com.dorm.manage.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 报修服务 - 提交、撤销、处理、评价、限流
 *
 * @author dorm-manage
 */
@Service
@RequiredArgsConstructor
public class RepairService {

    private final RepairMapper repairMapper;
    private final RepairEvaluationMapper repairEvaluationMapper;
    private final RedisUtil redisUtil;

    @Value("${dorm.rate-limit.repair-submit-max-per-minute:20}")
    private int repairSubmitMaxPerMinute;

    private static final int STATUS_PENDING = 0;
    private static final int STATUS_FINISHED = 2;
    private static final int STATUS_CANCELLED = 3;

    public String generateRepairNo() {
        return "R" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + (int) (Math.random() * 1000);
    }

    @Transactional(rollbackFor = Exception.class)
    public void submit(Repair repair, Long studentId, String clientIp) {
        if (!redisUtil.tryIncrementRateLimit("repair:" + clientIp, repairSubmitMaxPerMinute)) {
            throw new BusinessException("提交过于频繁，请稍后再试");
        }
        repair.setStudentId(studentId);
        repair.setRepairNo(generateRepairNo());
        repair.setStatus(STATUS_PENDING);
        repairMapper.insert(repair);
    }

    public void cancel(Long id, Long studentId) {
        Repair r = repairMapper.selectById(id);
        if (r == null || !r.getStudentId().equals(studentId)) {
            throw new BusinessException("无权操作");
        }
        if (r.getStatus() != STATUS_PENDING) {
            throw new BusinessException("仅待处理状态可撤销");
        }
        r.setStatus(STATUS_CANCELLED);
        repairMapper.updateById(r);
    }

    public PageResult<Repair> myList(Long studentId, Integer pageNum, Integer pageSize,
                                     Integer status, String repairType, String startTime, String endTime) {
        LocalDateTime start = startTime != null && !startTime.isEmpty() ? LocalDateTime.parse(startTime + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        LocalDateTime end = endTime != null && !endTime.isEmpty() ? LocalDateTime.parse(endTime + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        long offset = (long) (pageNum - 1) * pageSize;
        List<Repair> list = repairMapper.selectByStudentId(studentId, status, repairType, start, end, offset, pageSize);
        long total = repairMapper.countByStudentId(studentId, status, repairType, start, end);
        return new PageResult<>(total, list);
    }

    public Repair getById(Long id) {
        return repairMapper.selectById(id);
    }

    public void assignHandler(Long id, Long handlerId, String handlerName) {
        Repair r = repairMapper.selectById(id);
        if (r == null) {
            throw new BusinessException("报修单不存在");
        }
        r.setHandlerId(handlerId);
        r.setHandlerName(handlerName);
        r.setStatus(1);
        repairMapper.updateById(r);
    }

    public void handle(Long id, String handleRemark, String handleImages, boolean complete) {
        Repair r = repairMapper.selectById(id);
        if (r == null) {
            throw new BusinessException("报修单不存在");
        }
        r.setHandleRemark(handleRemark);
        r.setHandleImages(handleImages);
        if (complete) {
            r.setStatus(STATUS_FINISHED);
            r.setCompleteTime(LocalDateTime.now());
        } else {
            r.setStatus(1);
        }
        repairMapper.updateById(r);
    }

    public PageResult<Repair> adminPage(int pageNum, int pageSize, Integer status, String repairType,
                                       String buildingNo, Long handlerId, String startTime, String endTime) {
        LocalDateTime start = startTime != null && !startTime.isEmpty() ? LocalDateTime.parse(startTime + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        LocalDateTime end = endTime != null && !endTime.isEmpty() ? LocalDateTime.parse(endTime + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        long offset = (long) (pageNum - 1) * pageSize;
        List<Repair> list = repairMapper.selectPage(status, repairType, buildingNo, handlerId, start, end, offset, pageSize);
        long total = repairMapper.countPage(status, repairType, buildingNo, handlerId, start, end);
        return new PageResult<>(total, list);
    }

    public void evaluate(Long repairId, Long studentId, Integer score, String content) {
        Repair r = repairMapper.selectById(repairId);
        if (r == null || !r.getStudentId().equals(studentId)) {
            throw new BusinessException("无权评价");
        }
        if (r.getStatus() != STATUS_FINISHED) {
            throw new BusinessException("仅已完成的报修可评价");
        }
        if (repairEvaluationMapper.selectByRepairId(repairId) != null) {
            throw new BusinessException("已评价过");
        }
        RepairEvaluation ev = new RepairEvaluation();
        ev.setRepairId(repairId);
        ev.setStudentId(studentId);
        ev.setScore(score);
        ev.setContent(content);
        repairEvaluationMapper.insert(ev);
    }

    public List<Map<String, Object>> statsByType(String startTime, String endTime) {
        LocalDateTime start = startTime != null && !startTime.isEmpty() ? LocalDateTime.parse(startTime + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        LocalDateTime end = endTime != null && !endTime.isEmpty() ? LocalDateTime.parse(endTime + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        return repairMapper.countByType(start, end);
    }

    public List<Map<String, Object>> statsByBuilding(String startTime, String endTime) {
        LocalDateTime start = startTime != null && !startTime.isEmpty() ? LocalDateTime.parse(startTime + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        LocalDateTime end = endTime != null && !endTime.isEmpty() ? LocalDateTime.parse(endTime + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        return repairMapper.countByBuilding(start, end);
    }
}
