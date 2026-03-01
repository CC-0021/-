package com.dorm.manage.mapper;

import com.dorm.manage.entity.DormApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 宿舍申请 Mapper
 *
 * @author dorm-manage
 */
@Mapper
public interface DormApplicationMapper {

    int insert(DormApplication application);

    DormApplication selectById(Long id);

    List<DormApplication> selectByStudentId(Long studentId);

    List<DormApplication> selectPage(@Param("status") Integer status,
                                     @Param("applicationType") String applicationType,
                                     @Param("offset") long offset,
                                     @Param("limit") int limit);

    long countPage(@Param("status") Integer status, @Param("applicationType") String applicationType);

    int updateById(DormApplication application);
}
