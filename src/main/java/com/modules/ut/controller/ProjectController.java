package com.modules.ut.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.common.utils.Constant;
import com.common.utils.R;
import com.modules.sys.controller.AbstractController;
import com.modules.sys.entity.SysUserEntity;
import com.modules.sys.entity.SysUserRoleEntity;
import com.modules.sys.service.SysDepartmentService;
import com.modules.sys.service.SysUserRoleService;
import com.modules.sys.service.SysUserService;
import com.modules.ut.entity.Project;
import com.modules.ut.service.ProjectService;
import com.modules.ut.service.ProjectUserService;
import com.modules.ut.service.UTWriteService;
import com.modules.ut.util.ProjectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/project")
public class ProjectController extends AbstractController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private UTWriteService UTWriteService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysDepartmentService sysDepartmentService;
    //部门管理员
    @Value("${role.departmentAdminId}")
    private long departmentAdminId;
    //计划财务部角色id
    @Value("${role.planFinanceId}")
    private long planFinanceId;
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);

    /**
     * 查询项目列表
     */
    @PostMapping("/projectList")
    public R list(@RequestBody Map<String, Object> params) {
        String queryType = (String) params.get("queryType");
        SysUserEntity sysUserEntity = getUser();
        long userId = sysUserEntity.getUserId();
        if (userId != (long) Constant.SUPER_ADMIN) {
            if ("1".equals(queryType)) {
                //查询自己负责的项目
                params.put("userId", userId);
            } else {
                boolean departmentAdminFlag = false;
                boolean planFinanceFlag = false;
                List<SysUserRoleEntity> list = sysUserRoleService.lambdaQuery().eq(SysUserRoleEntity::getUserId, sysUserEntity.getUserId()).list();
                if (CollectionUtil.isNotEmpty(list)) {
                    for (SysUserRoleEntity sysUserRoleEntity : list) {
                        long roleId = sysUserRoleEntity.getRoleId();
                        if (departmentAdminId == roleId) {
                            //部门管理员id
                            departmentAdminFlag = true;
                        } else if (planFinanceId == roleId) {
                            //计划财务部id
                            planFinanceFlag = true;
                        }
                    }
                    if (departmentAdminFlag) {
                        //部门管理员查询自己部门所有的项目
                        params.put("departmentId", sysUserEntity.getDepartmentId());
                        params.put("roleType", "2");
                    } else if (planFinanceFlag) {
                        //计划财务部查询所有的
                        params.put("roleType", "4");
                    } else {
                        //其他角色不允许查询
                        return R.ok(new HashMap());
                    }
                }
            }
        } else {
            params.put("roleType", "0");
        }

        Map<String, Object> result = projectService.getProjectByMap(params);
        return R.ok(result);
    }

    /**
     * 更新项目总工时
     */
    @PostMapping("/updateProject")
    public R updateProject(@RequestBody Project project) {
        if (project.getId() == null || project.getUt().intValue() == 0) {
            logger.error("项目id和项目工时为空");
            return R.error("项目id和项目工时不能为空");
        }
        Project projectOld = projectService.getById(project.getId());
        projectOld.setUt(project.getUt());
        projectOld.setModifiedTime(new Date());
        projectService.updateById(projectOld);
        return R.ok();
    }

    /**
     * 上传项目
     */
    @PostMapping("/projectUpload")
    public R projectUpload(@RequestParam("file") MultipartFile file, @RequestParam("excelMonth") String excelMonth,@RequestParam("roleType") String roleType) {
        try {
            SysUserEntity sysUserEntity = getUser();
            EasyExcel.read(file.getInputStream(), Project.class,
                            new ProjectListener(projectService, projectUserService, sysUserService, sysDepartmentService, excelMonth,roleType,sysUserEntity))
                    .sheet().headRowNumber(2).doRead();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Excel解析出错");
            return R.error("导入模板文件不正确，请修改后，重新上传");
        }
        return R.ok();
    }


}
