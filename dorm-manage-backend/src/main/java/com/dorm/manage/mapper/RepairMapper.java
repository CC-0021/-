package com.dorm.manage.mapper;

import com.dorm.manage.entity.Repair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 报修 Mapper
 *
 * @author dorm-manage
 */
@Mapper
public interface RepairMapper {

    int insert(Repair repair);

    Repair selectById(Long id);

    Repair selectByRepairNo(String repairNo);

    List<Repair> selectByStudentId(@Param("studentId") Long studentId,
                                   @Param("status") Integer status,
                                   @Param("repairType") String repairType,
                                   @Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime,
                                   @Param("offset") long offset,
                                   @Param("limit") int limit);

    long countByStudentId(@Param("studentId") Long studentId,
                          @Param("status") Integer status,
                          @Param("repairType") String repairType,
                          @Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime);

    List<Repair> selectPage(@Param("status") Integer status,
                           @Param("repairType") String repairType,
                           @Param("buildingNo") String buildingNo,
                           @Param("handlerId") Long handlerId,
                           @Param("startTime") LocalDateTime startTime,
                           @Param("endTime") LocalDateTime endTime,
                           @Param("offset") long offset,
                           @Param("limit") int limit);

    long countPage(@Param("status") Integer status,
                   @Param("repairType") String repairType,
                   @Param("buildingNo") String buildingNo,
                   @Param("handlerId") Long handlerId,
                   @Param("startTime") LocalDateTime startTime,
                   @Param("endTime") LocalDateTime endTime);

    int updateById(Repair repair);

    List<Map<String, Object>> countByType(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<Map<String, Object>> countByBuilding(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
