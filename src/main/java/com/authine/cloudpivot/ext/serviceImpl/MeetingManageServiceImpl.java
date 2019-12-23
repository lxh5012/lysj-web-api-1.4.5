package com.authine.cloudpivot.ext.serviceImpl;

import com.authine.cloudpivot.ext.mapper.MeetingManageMapper;
import com.authine.cloudpivot.ext.reqDto.ReqMeetTime;
import com.authine.cloudpivot.ext.service.MeetingManageService;
import com.authine.cloudpivot.ext.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class MeetingManageServiceImpl implements MeetingManageService {
    @Autowired
    MeetingManageMapper manageMapper;

    @Override
    public List<String> getFreeMeetRoom(ReqMeetTime meetTime) {
        String startTime = DateUtils.getDateTimeToString(meetTime.getStartTime());
        String endTime = DateUtils.getDateTimeToString(meetTime.getEndTime());
        String meetTableName = "i0wbn_meeting";
        String meetRoomTableName = "ijy9l_meetingroommanage";
        List<String> userMeetRoom = manageMapper.getUseMeetRoom(meetTableName, startTime, endTime);
        List<String> allMeetRoom = manageMapper.getAllMeetRoom(meetRoomTableName);
        if (userMeetRoom.size() == 0) {
            return allMeetRoom;
        } else {
            List<String> distinctElements = userMeetRoom.stream().distinct().collect(toList());
            // 差集 (list1 - list2)
            List<String> reduce1 = allMeetRoom.stream().filter(item -> !distinctElements.contains(item)).collect(toList());
            return reduce1;
        }
    }
}
