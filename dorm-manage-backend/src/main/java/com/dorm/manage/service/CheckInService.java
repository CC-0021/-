package com.dorm.manage.service;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.entity.BedAssignLog;
import com.dorm.manage.entity.CheckIn;
import com.dorm.manage.exception.BusinessException;
import com.dorm.manage.mapper.BedAssignLogMapper;
import com.dorm.manage.mapper.BedMapper;
import com.dorm.manage.mapper.CheckInMapper;
import com.dorm.manage.mapper.UserMapper;
import com.dorm.manage.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 入住登记服务
 *
 * @author dorm-manage
 */
@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckInMapper checkInMapper;
    private final UserMapper userMapper;
    private final BedMapper bedMapper;
    private final BedAssignLogMapper bedAssignLogMapper;

    public void submit(CheckIn checkIn, Long studentId) {
        User user = userMapper.selectById(studentId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        checkIn.setStudentId(studentId);
        checkIn.setStudentNo(user.getUsername());
        checkIn.setRealName(user.getRealName());
        checkIn.setGender(user.getGender());
        checkIn.setCollege(user.getCollege());
        checkIn.setMajor(user.getMajor());
        checkIn.setClassName(user.getClassName());
        checkIn.setIdCard(user.getIdCard());
        checkIn.setStatus(0);
        checkInMapper.insert(checkIn);
    }

    public List<CheckIn> myList(Long studentId) {
        return checkInMapper.selectByStudentId(studentId);
    }

    public PageResult<CheckIn> adminPage(int pageNum, int pageSize, Integer status, String college) {
        long offset = (long) (pageNum - 1) * pageSize;
        List<CheckIn> list = checkInMapper.selectPage(status, college, offset, pageSize);
        long total = checkInMapper.countPage(status, college);
        return new PageResult<>(total, list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void audit(Long id, boolean pass, String rejectReason, Long auditUserId, Long bedId) {
        CheckIn c = checkInMapper.selectById(id);
        if (c == null) {
            throw new BusinessException("记录不存在");
        }
        c.setStatus(pass ? 1 : 2);
        c.setRejectReason(rejectReason);
        c.setAuditUserId(auditUserId);
        c.setAuditTime(LocalDateTime.now());
        c.setBedId(bedId);
        checkInMapper.updateById(c);
        // 审核通过后同步床位状态并记录分配日志
        if (pass && bedId != null) {
            bedMapper.updateStatus(bedId, 1, c.getStudentNo());
            BedAssignLog log = new BedAssignLog();
            log.setStudentId(c.getStudentId());
            log.setNewBedId(bedId);
            log.setAssignUserId(auditUserId);
            log.setAssignTime(LocalDateTime.now());
            log.setRemark("入住登记审核通过");
            bedAssignLogMapper.insert(log);
        }
    }
}
