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

    List<Violation> selectPage(@Param("studentId") String studentId,
                               @Param("roomNo") String roomNo,
                               @Param("handleStatus") String handleStatus,
                               @Param("offset") long offset,
                               @Param("limit") int limit);

    long countPage(@Param("studentId") String studentId,
                   @Param("roomNo") String roomNo,
                   @Param("handleStatus") String handleStatus);

    List<Violation> selectByStudentId(@Param("studentId") String studentId);

    int updateById(Violation violation);

    int deleteById(Long id);
}
