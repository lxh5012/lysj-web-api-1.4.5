package com.authine.cloudpivot.ext.serviceImpl;

import com.authine.cloudpivot.ext.mapper.MeetingManageMapper;
import com.authine.cloudpivot.ext.reqDto.ReqMeetTime;
import com.authine.cloudpivot.ext.service.MeetingManageService;
import com.authine.cloudpivot.ext.util.DateUtils;
import com.authine.cloudpivot.web.api.dubbo.DubboConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class MeetingManageServiceImpl implements MeetingManageService {
    @Autowired
    MeetingManageMapper manageMapper;
    @Autowired
    private DubboConfigService dubboConfigService;

    @Override
    public List<String> getFreeMeetRoom(ReqMeetTime meetTime) {
        String startTime = "";
        String endTime = "";
        try {
            startTime = this.getDateFormat(meetTime.getStartTime());
            endTime = this.getDateFormat(meetTime.getEndTime());
        } catch (Exception e) {

        }

        String meetTableName = "I0WBN_MEETING";
        String meetRoomTableName = "IJY9L_MEETINGROOMMANAGE";
//        String meetTableName = dubboConfigService.getBizObjectFacade().getTableName("MEETING");
//        String meetRoomTableName = dubboConfigService.getBizObjectFacade().getTableName("MEETINGROOMMANAGE");
        List<String> userMeetRoom = manageMapper.getUseMeetRoom(meetTableName, DateUtils.getStringToDate(startTime), DateUtils.getStringToDate(endTime));
//        List<String> userMeetRoom = new ArrayList<>();
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

    public String getDateFormat(String create_time) throws Exception {
        String format = "";
        if (create_time != null && create_time != "NULL" && create_time != "") {
            if (isDate(create_time)) {
                format = create_time;
            } else {
                //转换日期格式(将Mon Jun 18 2018 00:00:00 GMT+0800 (中国标准时间) 转换成yyyy-MM-dd)
                create_time = create_time.replace("Z", " UTC");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                Date d = sdf1.parse(create_time);//Mon Mar 06 00:00:00 CST 2017
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                format = sdf.format(d);//2017-03-06
            }
        }

        return format;
    }

    private static boolean isDate(String date) {
        /**
         * 判断日期格式和范围
         */
        String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(date);
        boolean dateType = mat.matches();
        return dateType;
    }
}
