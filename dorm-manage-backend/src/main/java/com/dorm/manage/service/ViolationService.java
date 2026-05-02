package com.dorm.manage.service;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.entity.Violation;
import com.dorm.manage.mapper.ViolationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public PageResult<Violation> page(int pageNum, int pageSize, Long studentId, String roomNo) {
        long total = violationMapper.countPage(studentId, roomNo);
        int offset = (pageNum - 1) * pageSize;
        List<Violation> list = violationMapper.selectPage(studentId, roomNo, offset, pageSize);
        return new PageResult<>(total, list);
    }

    /**
     * 根据ID获取违规记录
     */
    public Violation getById(Long id) {
        return violationMapper.selectById(id);
    }

    /**
     * 新增违规记录
     */
    public void add(Violation violation) {
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
     * 学生端：查询自己的违规记录
     */
    public List<Violation> myList(Long studentId) {
        return violationMapper.selectByStudentId(studentId);
    }
}
