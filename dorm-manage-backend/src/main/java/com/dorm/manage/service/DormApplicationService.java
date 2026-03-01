package com.dorm.manage.service;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.entity.DormApplication;
import com.dorm.manage.exception.BusinessException;
import com.dorm.manage.mapper.DormApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 宿舍申请服务
 *
 * @author dorm-manage
 */
@Service
@RequiredArgsConstructor
public class DormApplicationService {

    private final DormApplicationMapper dormApplicationMapper;

    public void submit(DormApplication application, Long studentId) {
        if (application.getReason() == null || application.getReason().length() < 20) {
            throw new BusinessException("申请理由至少20字");
        }
        application.setStudentId(studentId);
        application.setStatus(0);
        dormApplicationMapper.insert(application);
    }

    public List<DormApplication> myList(Long studentId) {
        return dormApplicationMapper.selectByStudentId(studentId);
    }

    public PageResult<DormApplication> adminPage(int pageNum, int pageSize, Integer status, String applicationType) {
        long offset = (long) (pageNum - 1) * pageSize;
        List<DormApplication> list = dormApplicationMapper.selectPage(status, applicationType, offset, pageSize);
        long total = dormApplicationMapper.countPage(status, applicationType);
        return new PageResult<>(total, list);
    }

    public void audit(Long id, boolean pass, String rejectReason, Long auditUserId) {
        DormApplication a = dormApplicationMapper.selectById(id);
        if (a == null) {
            throw new BusinessException("申请不存在");
        }
        a.setStatus(pass ? 1 : 2);
        a.setRejectReason(rejectReason);
        a.setAuditUserId(auditUserId);
        a.setAuditTime(java.time.LocalDateTime.now());
        dormApplicationMapper.updateById(a);
    }
}
