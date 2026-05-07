package com.dorm.manage.service;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.common.ResultCode;
import com.dorm.manage.entity.Violation;
import com.dorm.manage.exception.BusinessException;
import com.dorm.manage.mapper.ViolationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 宿舍违规管理服务
 *
 * @author dorm-manage
 */
@Service
@RequiredArgsConstructor
public class ViolationService {

    private final ViolationMapper violationMapper;

    /**
     * 分页查询违规记录
     */
    public PageResult<Violation> page(int pageNum, int pageSize, String studentId, String roomNo, String handleStatus) {
        long total = violationMapper.countPage(studentId, roomNo, handleStatus);
        int offset = (pageNum - 1) * pageSize;
        List<Violation> list = violationMapper.selectPage(studentId, roomNo, handleStatus, offset, pageSize);
        return new PageResult<>(total, list);
    }

    /**
     * 根据ID获取违规记录
     */
    public Violation getById(Long id) {
        return violationMapper.selectById(id);
    }

    /**
     * 新增违规记录（studentId 直接存储学号）
     */
    public void add(Violation violation) {
        if (violation.getStudentId() == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "学号不能为空");
        }
        violationMapper.insert(violation);
    }

    /**
     * 更新违规记录
     */
    public void update(Violation violation) {
        violationMapper.updateById(violation);
    }

    /**
     * 删除违规记录
     */
    public void delete(Long id) {
        violationMapper.deleteById(id);
    }

    /**
     * 批量删除违规记录
     */
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            violationMapper.deleteById(id);
        }
    }

    /**
     * 学生端：查询自己的违规记录（支持按状态筛选）
     */
    public List<Violation> myList(String username, String status) {
        List<Violation> list = violationMapper.selectByStudentId(username);
        if ("pending".equals(status)) {
            return list.stream()
                    .filter(v -> v.getHandleResult() == null || v.getHandleResult().isEmpty())
                    .collect(Collectors.toList());
        }
        if ("handled".equals(status)) {
            return list.stream()
                    .filter(v -> v.getHandleResult() != null && !v.getHandleResult().isEmpty())
                    .collect(Collectors.toList());
        }
        return list;
    }
}
