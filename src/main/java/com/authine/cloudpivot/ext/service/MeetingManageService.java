package com.authine.cloudpivot.ext.service;

import com.authine.cloudpivot.ext.reqDto.ReqMeetTime;

import java.util.List;

public interface MeetingManageService {
    List<String> getFreeMeetRoom(ReqMeetTime meetTime);
}
