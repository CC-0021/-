package com.dorm.manage.mapper;

import com.dorm.manage.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公告 Mapper
 *
 * @author dorm-manage
 */
@Mapper
public interface AnnouncementMapper {

    List<Announcement> selectPage(@Param("title") String title,
                                  @Param("status") Integer status,
                                  @Param("offset") long offset,
                                  @Param("limit") int limit);

    long countPage(@Param("title") String title, @Param("status") Integer status);

    Announcement selectById(Long id);

    int insert(Announcement announcement);

    int updateById(Announcement announcement);

    int deleteById(Long id);

    int countTop();

    List<Announcement> selectPublishedRecent(@Param("days") int days, @Param("limit") int limit);
}
