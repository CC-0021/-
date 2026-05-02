package com.dorm.manage.service;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.entity.Bed;
import com.dorm.manage.mapper.BedMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 床位管理服务
 *
 * @author dorm-manage
 */
@Service
@RequiredArgsConstructor
public class BedService {

    private final BedMapper bedMapper;

    /**
     * 分页查询床位列表
     */
    public PageResult<Bed> page(int pageNum, int pageSize, Long dormitoryId, String bedNo, Integer status) {
        long total = bedMapper.countPage(dormitoryId, bedNo, status);
        int offset = (pageNum - 1) * pageSize;
        List<Bed> list = bedMapper.selectPage(dormitoryId, bedNo, status, offset, pageSize);
        return new PageResult<>(total, list);
    }

    /**
     * 根据宿舍ID获取床位列表
     */
    public List<Bed> getByDormitoryId(Long dormitoryId) {
        return bedMapper.selectByDormitoryId(dormitoryId);
    }

    /**
     * 根据ID获取床位信息
     */
    public Bed getById(Long id) {
        return bedMapper.selectById(id);
    }

    /**
     * 新增床位
     */
    public void add(Bed bed) {
        bedMapper.insert(bed);
    }

    /**
     * 更新床位信息
     */
    public void update(Bed bed) {
        bedMapper.updateById(bed);
    }

    /**
     * 更新床位状态
     */
    public void updateStatus(Long id, Integer status, Long studentId) {
        bedMapper.updateStatus(id, status, studentId);
    }

    /**
     * 删除床位
     */
    public void delete(Long id) {
        bedMapper.deleteById(id);
    }

    /**
     * 批量删除床位
     */
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            bedMapper.deleteById(id);
        }
    }
}