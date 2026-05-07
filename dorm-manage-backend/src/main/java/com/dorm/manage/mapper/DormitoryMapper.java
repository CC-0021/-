package com.dorm.manage.mapper;

import com.dorm.manage.entity.Dormitory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 宿舍 Mapper
 *
 * @author dorm-manage
 */
@Mapper
public interface DormitoryMapper {

    List<Dormitory> selectPage(@Param("buildingNo") String buildingNo,
                               @Param("roomNo") String roomNo,
                               @Param("status") Integer status,
                               @Param("offset") long offset,
                               @Param("limit") int limit);

    long countPage(@Param("buildingNo") String buildingNo, @Param("roomNo") String roomNo, @Param("status") Integer status);

    Dormitory selectById(Long id);

    Dormitory selectByRoomNo(@Param("roomNo") Long roomNo);

    int insert(Dormitory dormitory);

    int updateById(Dormitory dormitory);

    int countAvailableBeds();

    int deleteById(Long id);
}
