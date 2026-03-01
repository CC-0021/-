package com.dorm.manage.mapper;

import com.dorm.manage.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper
 *
 * @author dorm-manage
 */
@Mapper
public interface UserMapper {

    User selectById(Long id);

    User selectByUsername(@Param("username") String username);

    List<Long> selectRoleIdsByUserId(Long userId);

    List<String> selectPermissionCodesByUserId(Long userId);

    int insert(User user);

    int updateById(User user);
}
