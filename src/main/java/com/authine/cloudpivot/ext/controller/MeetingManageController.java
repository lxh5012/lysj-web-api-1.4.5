package com.authine.cloudpivot.ext.controller;

import com.authine.cloudpivot.ext.service.MeetingManageService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/getTest")
    public List<String> getTest() {
        return meetingManageService.getTest();
    }
}
