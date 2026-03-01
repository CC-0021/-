package com.dorm.manage.mapper;

import com.dorm.manage.entity.Violation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 违规记录 Mapper
 *
 * @author dorm-manage
 */
@Mapper
public interface ViolationMapper {

    int insert(Violation violation);

    Violation selectById(Long id);

    List<Violation> selectPage(@Param("studentId") Long studentId,
                               @Param("roomNo") String roomNo,
                               @Param("offset") long offset,
                               @Param("limit") int limit);

    long countPage(@Param("studentId") Long studentId, @Param("roomNo") String roomNo);

    int updateById(Violation violation);

    int deleteById(Long id);
}
