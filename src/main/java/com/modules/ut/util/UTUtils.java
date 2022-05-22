/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.modules.ut.util;

import cn.hutool.core.collection.CollectionUtil;
import com.common.exception.RRException;
import com.common.utils.Constant;
import com.modules.job.entity.ScheduleJobEntity;
import com.modules.sys.entity.SysUserEntity;
import com.modules.sys.entity.SysUserRoleEntity;
import com.modules.sys.service.SysUserRoleService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * UT工具类
 *
 */
@Component
public class UTUtils {
    //项目负责人
    private static long projectManagerId;
    @Value("${role.projectManagerId}")
    public void setProjectManagerId(long projectManagerId) {
        this.projectManagerId = projectManagerId;
    }


    //部门管理员
    private static long departmentAdminId;
    @Value("${role.departmentAdminId}")
    public void setDepartmentAdminId(long departmentAdminId) {
        this.departmentAdminId = departmentAdminId;
    }

    //所领导角色id
    private static long departmentLeaderId;
    @Value("${role.departmentLeaderId}")
    public void setDepartmentLeaderId(long departmentLeaderId) {
        this.departmentLeaderId = departmentLeaderId;
    }

    //人力资源部角色id
    private static long renLiId;
    @Value("${role.renLiId}")
    public void setRenLiId(long renLiId) {
        this.renLiId = renLiId;
    }

    //计划财务部角色id
    private static long planFinanceId;
    @Value("${role.planFinanceId}")
    public void setPlanFinanceId(long planFinanceId) {
        this.planFinanceId = planFinanceId;
    }
    //院领导角色id
    private static long companyLeaderId;
    @Value("${role.companyLeaderId}")
    public void setCompanyLeaderId(long companyLeaderId) {
        this.companyLeaderId = companyLeaderId;
    }

    private static SysUserRoleService sysUserRoleService;
    @Autowired
    public void setService(SysUserRoleService sysUserRoleService) {
        this.sysUserRoleService = sysUserRoleService;
    }
    public static String getRoleType(SysUserEntity sysUserEntity) {
        if(sysUserEntity.getUserId()!=(long) Constant.SUPER_ADMIN){
            List<SysUserRoleEntity> list = sysUserRoleService.lambdaQuery().eq(SysUserRoleEntity::getUserId, sysUserEntity.getUserId()).list();
            if (CollectionUtil.isNotEmpty(list)) {
                for (SysUserRoleEntity sysUserRoleEntity : list) {
                    long roleId = sysUserRoleEntity.getRoleId();
                    if (projectManagerId == roleId) {
                        //项目负责人id
                        return "1";
                    }else if (departmentAdminId == roleId) {
                        //部门管理员id
                        return "2";
                    } else if (departmentLeaderId == roleId) {
                        //所领导角色id
                        return "3";
                    } else if (planFinanceId == roleId) {
                        //计划财务部id
                        return "4";
                    }else if (companyLeaderId == roleId) {
                        //院领导角色
                        return "5";
                    }else if (renLiId == roleId) {
                        //人力角色id
                        return "6";
                    }
                }
            }
        }else {
            return "0";
        }

        return "-1";
    }
    

}
