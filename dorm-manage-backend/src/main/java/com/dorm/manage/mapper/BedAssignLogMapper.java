package com.dorm.manage.mapper;

import com.dorm.manage.entity.BedAssignLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BedAssignLogMapper {

    int insert(BedAssignLog log);
}
