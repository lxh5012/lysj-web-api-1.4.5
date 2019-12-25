package com.authine.cloudpivot.ext.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MeetingManageMapper {
    List<String> getUseMeetRoom(@Param("tableName") String tableName,@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<String> getAllMeetRoom(@Param("tableName") String tableName);
}
