package com.dorm.manage.service;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.entity.Dormitory;
import com.dorm.manage.mapper.DormitoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 宿舍管理服务
 *
 * @author dorm-manage
 */
@Service
@RequiredArgsConstructor
public class DormitoryService {

    private final DormitoryMapper dormitoryMapper;

    /**
     * 分页查询宿舍列表
     */
    public PageResult<Dormitory> page(int pageNum, int pageSize, String buildingNo, String roomNo, Integer status) {
        long total = dormitoryMapper.countPage(buildingNo, roomNo, status);
        int offset = (pageNum - 1) * pageSize;
        List<Dormitory> list = dormitoryMapper.selectPage(buildingNo, roomNo, status, offset, pageSize);
        return new PageResult<>(total, list);
    }

    /**
     * 根据ID获取宿舍信息
     */
    public Dormitory getById(Long id) {
        return dormitoryMapper.selectById(id);
    }

    /**
     * 根据房间号获取宿舍信息
     */
    public Dormitory getByRoomNo(Long roomNo) {
        return dormitoryMapper.selectByRoomNo(roomNo);
    }

    /**
     * 新增宿舍：根据宿舍类型自动设置床位总数和空闲床位
     */
    public void add(Dormitory dormitory) {
        if ("6人间".equals(dormitory.getDormType())) {
            dormitory.setBedTotal(6);
            dormitory.setBedAvailable(6);
        } else {
            dormitory.setBedTotal(4);
            dormitory.setBedAvailable(4);
        }
        dormitoryMapper.insert(dormitory);
    }

    /**
     * 更新宿舍信息
     */
    public void update(Dormitory dormitory) {
        dormitoryMapper.updateById(dormitory);
    }

    /**
     * 删除宿舍
     */
    public void delete(Long id) {
        dormitoryMapper.deleteById(id);
    }

    /**
     * 批量删除宿舍
     */
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            dormitoryMapper.deleteById(id);
        }
    }
}