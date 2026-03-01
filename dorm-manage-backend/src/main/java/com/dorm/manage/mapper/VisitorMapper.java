package com.dorm.manage.mapper;

import com.dorm.manage.entity.Visitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 来访登记 Mapper
 *
 * @author dorm-manage
 */
@Mapper
public interface VisitorMapper {

    int insert(Visitor visitor);

    Visitor selectById(Long id);

    List<Visitor> selectPage(@Param("roomNo") String roomNo,
                             @Param("startTime") LocalDateTime startTime,
                             @Param("endTime") LocalDateTime endTime,
                             @Param("offset") long offset,
                             @Param("limit") int limit);

    long countPage(@Param("roomNo") String roomNo,
                   @Param("startTime") LocalDateTime startTime,
                   @Param("endTime") LocalDateTime endTime);

    int updateById(Visitor visitor);

    List<Map<String, Object>> countByDay(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Map<String, Object>> countByBuilding(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
