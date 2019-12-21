package com.authine.cloudpivot.ext.service;

import com.authine.cloudpivot.engine.api.model.runtime.WorkItemModel;
import com.authine.cloudpivot.ext.domain.Jc;
import com.authine.cloudpivot.ext.domain.Wl;

import java.util.List;
import java.util.Map;

/**
 * @author Boliy 2019/11/08 15:58
 */
public interface BaseService {

    Wl selectWl(String gxbBm, String gxbLx);

    String getMapUserName(String gxbDhxtyh);

    List<Jc> selectJcList(String typeKey);

    Jc getJc(String id);

    String getJcValue(String id);

    String getName(String userId);

    String getDeptName(String deptId);

    String getUserOrDeptName(String id);

    Map<String, List<WorkItemModel>> getWorkItemMaps(String workflowInstanceId, Boolean isFinish);

}
