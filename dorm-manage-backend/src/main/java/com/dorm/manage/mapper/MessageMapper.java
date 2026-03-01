package com.dorm.manage.mapper;

import com.dorm.manage.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 留言 Mapper
 *
 * @author dorm-manage
 */
@Mapper
public interface MessageMapper {

    int insert(Message message);

    Message selectById(Long id);

    List<Message> selectByStudentId(Long studentId);

    List<Message> selectPage(@Param("status") Integer status,
                             @Param("offset") long offset,
                             @Param("limit") int limit);

    long countPage(@Param("status") Integer status);

    int updateById(Message message);

    int deleteById(Long id);
}
