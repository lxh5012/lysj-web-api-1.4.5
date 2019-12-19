package com.authine.cloudpivot.ext.serviceImpl;

import com.authine.cloudpivot.ext.mapper.MeetingManageMapper;
import com.authine.cloudpivot.ext.service.MeetingManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingManageServiceImpl implements MeetingManageService {
    @Autowired
    MeetingManageMapper manageMapper;

    @Override
    public List<String> getTest() {
        List<String> c = manageMapper.getUserId();
        return c;
    }
}
