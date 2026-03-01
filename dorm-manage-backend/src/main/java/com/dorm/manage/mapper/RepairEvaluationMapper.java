package com.dorm.manage.mapper;

import com.dorm.manage.entity.RepairEvaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 报修评价 Mapper
 *
 * @author dorm-manage
 */
@Mapper
public interface RepairEvaluationMapper {

    int insert(RepairEvaluation evaluation);

    RepairEvaluation selectByRepairId(Long repairId);

    int deleteByRepairId(Long repairId);
}
