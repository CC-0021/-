package com.dorm.manage.mapper;

import com.dorm.manage.entity.Bed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 床位 Mapper
 *
 * @author dorm-manage
 */
@Mapper
public interface BedMapper {

    List<Bed> selectByDormitoryId(Long dormitoryId);

    Bed selectById(Long id);

    List<Bed> selectPage(@Param("dormitoryId") Long dormitoryId,
                         @Param("bedNo") String bedNo,
                         @Param("status") Integer status,
                         @Param("offset") long offset,
                         @Param("limit") int limit);

    long countPage(@Param("dormitoryId") Long dormitoryId, @Param("bedNo") String bedNo, @Param("status") Integer status);

    int insert(Bed bed);

    int updateById(Bed bed);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("studentId") String studentId);

    int deleteById(Long id);

    Bed selectByStudentId(@Param("studentId") String studentId);
}
