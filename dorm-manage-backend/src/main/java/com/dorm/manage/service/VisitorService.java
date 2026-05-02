package com.dorm.manage.service;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.entity.Visitor;
import com.dorm.manage.mapper.VisitorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 来访登记服务
 *
 * @author dorm-manage
 */
@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorMapper visitorMapper;

    /**
     * 分页查询来访记录
     */
    public PageResult<Visitor> page(int pageNum, int pageSize, String roomNo, LocalDateTime startTime, LocalDateTime endTime) {
        long total = visitorMapper.countPage(roomNo, startTime, endTime);
        int offset = (pageNum - 1) * pageSize;
        List<Visitor> list = visitorMapper.selectPage(roomNo, startTime, endTime, offset, pageSize);
        return new PageResult<>(total, list);
    }

    /**
     * 根据ID获取来访记录
     */
    public Visitor getById(Long id) {
        return visitorMapper.selectById(id);
    }

    /**
     * 新增来访记录
     */
    public void add(Visitor visitor) {
        visitorMapper.insert(visitor);
    }

    /**
     * 更新来访记录
     */
    public void update(Visitor visitor) {
        visitorMapper.updateById(visitor);
    }

    /**
     * 按天统计来访数量
     */
    public List<Map<String, Object>> countByDay(LocalDateTime start, LocalDateTime end) {
        return visitorMapper.countByDay(start, end);
    }

    /**
     * 按楼栋统计来访数量
     */
    public List<Map<String, Object>> countByBuilding(LocalDateTime start, LocalDateTime end) {
        return visitorMapper.countByBuilding(start, end);
    }
}