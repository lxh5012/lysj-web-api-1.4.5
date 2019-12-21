package com.authine.cloudpivot.ext.mapper;


import com.authine.cloudpivot.ext.domain.BaseUser;
import com.authine.cloudpivot.ext.domain.Jc;
import com.authine.cloudpivot.ext.domain.Wl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseMapper {
    /**
     * 获取外键信息
     *
     * @param tableName
     * @param gxbBm
     * @return
     */
    Wl selectWl(String tableName, String gxbBm, String gxbLx);

    /**
     * 获取关系表中的用户名
     *
     * @param tableName
     * @param gxbDhxtyh
     * @return
     */
    String getUserName(String tableName, String gxbDhxtyh);

    /**
     * 获取数据字典信息
     *
     * @param tableName
     * @param id
     * @return
     */
    Jc selectJc(String tableName, String id);

    /**
     * 根据类型获取数据字典信息
     *
     * @param tableName
     * @param typeKey
     * @return
     */
    List<Jc> selectJcList(String tableName, String typeKey);

    /**
     * 获取所有用户名称
     * @return
     */
    List<BaseUser> selectNameList();
}