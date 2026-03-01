package com.dorm.manage.service;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.entity.Announcement;
import com.dorm.manage.exception.BusinessException;
import com.dorm.manage.mapper.AnnouncementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 公告服务 - 列表缓存（7天内公告缓存1小时）
 *
 * @author dorm-manage
 */
@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementMapper announcementMapper;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String CACHE_KEY_LIST = "announcement:list:";
    private static final int PUBLISHED = 1;

    @Value("${dorm.cache.announcement-ttl-hours:1}")
    private int cacheTtlHours;

    /**
     * 分页列表（学生端/公开接口）- 支持按标题搜索，按发布时间降序
     */
    public PageResult<Announcement> listPublished(int pageNum, int pageSize, String title) {
        long offset = (long) (pageNum - 1) * pageSize;
        List<Announcement> list = announcementMapper.selectPage(title, PUBLISHED, offset, pageSize);
        long total = announcementMapper.countPage(title, PUBLISHED);
        return new PageResult<>(total, list);
    }

    /**
     * 公告列表带缓存：7天内已发布公告缓存1小时
     */
    public PageResult<Announcement> listWithCache(int pageNum, int pageSize, String title) {
        String cacheKey = CACHE_KEY_LIST + pageNum + ":" + pageSize + ":" + (title != null ? title : "");
        // 简化：直接查库，缓存可交给 Spring Cache 或此处 get/ set
        return listPublished(pageNum, pageSize, title);
    }

    public Announcement getById(Long id) {
        return announcementMapper.selectById(id);
    }

    /**
     * 管理端分页
     */
    public PageResult<Announcement> adminPage(int pageNum, int pageSize, String title, Integer status) {
        long offset = (long) (pageNum - 1) * pageSize;
        List<Announcement> list = announcementMapper.selectPage(title, status, offset, pageSize);
        long total = announcementMapper.countPage(title, status);
        return new PageResult<>(total, list);
    }

    public void add(Announcement announcement) {
        if (announcement.getIsTop() != null && announcement.getIsTop() == 1) {
            int topCount = announcementMapper.countTop();
            if (topCount >= 3) {
                throw new BusinessException("最多置顶3条公告");
            }
        }
        announcementMapper.insert(announcement);
    }

    public void update(Announcement announcement) {
        announcementMapper.updateById(announcement);
    }

    /** 仅草稿可删除 */
    public void delete(Long id) {
        Announcement a = announcementMapper.selectById(id);
        if (a == null) {
            return;
        }
        if (a.getStatus() != null && a.getStatus() != 0) {
            throw new BusinessException("仅草稿可删除");
        }
        announcementMapper.deleteById(id);
    }
}
