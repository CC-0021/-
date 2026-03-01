package com.dorm.manage.mapper;

import com.dorm.manage.entity.CheckIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入住登记 Mapper
 *
 * @author dorm-manage
 */
@Mapper
public interface CheckInMapper {

    int insert(CheckIn checkIn);

    CheckIn selectById(Long id);

    List<CheckIn> selectByStudentId(Long studentId);

    List<CheckIn> selectPage(@Param("status") Integer status,
                             @Param("college") String college,
                             @Param("offset") long offset,
                             @Param("limit") int limit);

    long countPage(@Param("status") Integer status, @Param("college") String college);

    int updateById(CheckIn checkIn);
}
