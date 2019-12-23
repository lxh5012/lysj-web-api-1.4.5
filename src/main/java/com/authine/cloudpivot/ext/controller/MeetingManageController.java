package com.authine.cloudpivot.ext.controller;

import com.authine.cloudpivot.ext.reqDto.ReqMeetTime;
import com.authine.cloudpivot.ext.service.MeetingManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会议管理控制类
 */
@RestController
@RequestMapping("/meetingManage")
public class MeetingManageController {
    private static final Logger log = LoggerFactory.getLogger(MeetingManageController.class);
    @Autowired
    MeetingManageService meetingManageService;


    // 获取空闲会议室
    @PostMapping("/getFreeMeetRoom")
    public List<String> getFreeMeetRoom(@RequestBody ReqMeetTime meetTime) {
        return  meetingManageService.getFreeMeetRoom(meetTime);
    }
}
