package com.authine.cloudpivot.ext.reqDto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ReqMeetTime {
    private Date startTime;
    private Date endTime;
}
