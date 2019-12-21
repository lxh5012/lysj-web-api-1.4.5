package com.authine.cloudpivot.ext.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.authine.cloudpivot.engine.api.facade.OrganizationFacade;
import com.authine.cloudpivot.engine.api.model.organization.DepartmentModel;
import com.authine.cloudpivot.engine.api.model.organization.UserModel;
import com.authine.cloudpivot.engine.api.model.runtime.WorkItemModel;

import com.authine.cloudpivot.ext.constant.SheetCodeConstant;
import com.authine.cloudpivot.ext.domain.BaseUser;
import com.authine.cloudpivot.ext.domain.Jc;
import com.authine.cloudpivot.ext.domain.Wl;
import com.authine.cloudpivot.ext.mapper.BaseMapper;
import com.authine.cloudpivot.ext.service.BaseService;
import com.authine.cloudpivot.web.api.dubbo.DubboConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BaseServiceImpl implements BaseService {
    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);
    private static final String CACHE_KEY_PREFIX_SGS_JC = "cloudpivot:webapi:cache:sgs:jc:";
    private static final String CACHE_KEY_PREFIX_SGS_USER = "cloudpivot:webapi:cache:sgs:user:";
    private static final String CACHE_KEY_PREFIX_SGS_DEPT = "cloudpivot:webapi:cache:sgs:dept:";

    @Autowired
    private BaseMapper baseMapper;
    @Autowired
    private DubboConfigService dubboConfigService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Wl selectWl(String gxbBm, String gxbLx) {
        return baseMapper.selectWl(
                dubboConfigService.getBizObjectFacade().getTableName(SheetCodeConstant.SHEET_CODE_WL),
                gxbBm, gxbLx);
    }

    @Override
    public String getMapUserName(String gxbDhxtyh) {
        return baseMapper.getUserName(
                dubboConfigService.getBizObjectFacade().getTableName(SheetCodeConstant.SHEET_CODE_DHXT_YH),
                gxbDhxtyh);
    }

    @Override
    public List<Jc> selectJcList(String typeKey) {
        if (StringUtils.isNoneBlank(typeKey)) {
            String key = CACHE_KEY_PREFIX_SGS_JC + typeKey;
            String value = this.redisTemplate.opsForValue().get(key);
            if (StringUtils.isNoneBlank(value)) {
                return JSON.parseObject(value, new TypeReference<List<Jc>>() {
                }, Feature.AutoCloseSource);
            } else {
                List<Jc> jcs = baseMapper.selectJcList(
                        dubboConfigService.getBizObjectFacade().getTableName(SheetCodeConstant.SHEET_CODE_JC),
                        typeKey);
                if(!CollectionUtils.isEmpty(jcs)){

                this.redisTemplate.opsForValue().set(CACHE_KEY_PREFIX_SGS_JC + typeKey, JSON.toJSONString(jcs), 24L, TimeUnit.HOURS);
                }
                return jcs;
            }
        }
        return null;
    }

    /**
     * 获取redis中的基础信息
     *
     * @param id
     * @return
     */
    @Override
    public Jc getJc(String id) {
        if (StringUtils.isNoneBlank(id)) {
            String key = CACHE_KEY_PREFIX_SGS_JC + id;
            String value = this.redisTemplate.opsForValue().get(key);
            if (StringUtils.isNoneBlank(value)) {
                return JSON.parseObject(value, new TypeReference<Jc>() {
                }, Feature.AutoCloseSource);
            } else {
                Jc jc = baseMapper.selectJc(
                        dubboConfigService.getBizObjectFacade().getTableName(SheetCodeConstant.SHEET_CODE_JC),
                        id);
                if (!ObjectUtils.isEmpty(jc)) {
                    //this.redisTemplate.opsForValue().set(key, JSON.toJSONString(jc), 2L, TimeUnit.MINUTES);
                    new Thread(() -> {
                        List<Jc> jcs = selectJcList(jc.getTypeKey());
                        for (Jc j : jcs) {
                            this.redisTemplate.opsForValue().set(CACHE_KEY_PREFIX_SGS_JC + j.getId(), JSON.toJSONString(j), 24L, TimeUnit.HOURS);
                        }
                    }).start();
                    return jc;
                }

            }
        }
        return null;
    }

    @Override
    public String getJcValue(String id) {
        Jc jc = getJc(id);
        if (!ObjectUtils.isEmpty(jc)) {
            return jc.getValue();
        }
        return "";
    }

    /**
     * 根据用户id获取名称
     *
     * @param userId
     * @return
     */
    @Override
    public String getName(String userId) {
        if (StringUtils.isNoneBlank(userId)) {
            String key = CACHE_KEY_PREFIX_SGS_USER + userId;
            String value = this.redisTemplate.opsForValue().get(key);
            if (StringUtils.isNoneBlank(value)) {
                return value;
            } else {
                UserModel user = getOrganizationFacade().getUser(userId);
                if (!ObjectUtils.isEmpty(user)) {
                    new Thread(() -> {
                        List<BaseUser> users = baseMapper.selectNameList();
                        if (!CollectionUtils.isEmpty(users)) {
                            for (BaseUser u : users) {
                                this.redisTemplate.opsForValue().set(CACHE_KEY_PREFIX_SGS_USER + u.getId(), u.getName(), 24L, TimeUnit.HOURS);
                            }
                        }

                    }).start();
                    return user.getName();
                }
            }
        }
        return "";
    }

    /**
     * 根据部门id获取名称
     *
     * @param deptId
     * @return
     */
    @Override
    public String getDeptName(String deptId) {
        if (StringUtils.isNoneBlank(deptId)) {
            String key = CACHE_KEY_PREFIX_SGS_DEPT + deptId;
            String value = this.redisTemplate.opsForValue().get(key);
            if (StringUtils.isNoneBlank(value)) {
                return value;
            } else {
                DepartmentModel dept = getOrganizationFacade().getDepartment(deptId);
                if (!ObjectUtils.isEmpty(dept)) {
                    new Thread(() -> {
                        List<DepartmentModel> depts = getOrganizationFacade().getDepartments();
                        if (!CollectionUtils.isEmpty(depts)) {
                            for (DepartmentModel d : depts) {
                                this.redisTemplate.opsForValue().set(CACHE_KEY_PREFIX_SGS_DEPT + d.getId(), d.getName(), 24L, TimeUnit.HOURS);
                            }
                        }
                    }).start();
                    return dept.getName();
                }
            }
        }
        return "";
    }

    /**
     * 根据ID获取用户名称或者部门名称
     *
     * @param id
     * @return
     */
    @Override
    public String getUserOrDeptName(String id) {
        String name = getName(id);
        if (StringUtils.isBlank(name)) {
            name = getDeptName(id);
        }
        return name;
    }

    /**
     * 获取待办或者已办集合
     *
     * @param workflowInstanceId
     * @param isFinish
     * @return
     */
    @Override
    public Map<String, List<WorkItemModel>> getWorkItemMaps(String workflowInstanceId, Boolean isFinish) {
        List<WorkItemModel> workItems = dubboConfigService.getWorkflowInstanceFacade().getWorkItems(workflowInstanceId, isFinish);
        Map<String, List<WorkItemModel>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(workItems)) {
            workItems.stream().forEach(obj -> {
                List<WorkItemModel> workItemModels = map.get(obj.getActivityCode());
                if (ObjectUtils.isEmpty(workItemModels)) {
                    workItemModels = new ArrayList<>();
                }
                workItemModels.add(obj);
                map.put(obj.getActivityCode(), workItemModels);
            });
        }
        return map;
    }

    protected OrganizationFacade getOrganizationFacade() {
        return dubboConfigService.getOrganizationFacade();
    }
}
