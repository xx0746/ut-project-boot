package com.modules.ut.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.common.utils.PageUtils;
import com.modules.sys.entity.SysDepartmentEntity;
import com.modules.sys.entity.SysUserEntity;
import com.modules.sys.service.SysDepartmentService;
import com.modules.sys.service.SysUserService;
import com.modules.ut.entity.Project;
import com.modules.ut.entity.ProjectUser;
import com.modules.ut.service.ProjectService;
import com.modules.ut.service.ProjectUserService;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;

public class ProjectListener extends AnalysisEventListener<Project> {
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 10;

    /**
     * 这个集合用于接收 读取Excel文件得到的数据
     */
    List<Project> list = new ArrayList<Project>();

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private ProjectService projectService;
    private ProjectUserService projectUserService;
    private String excelMonth;
    private SysUserService sysUserService;
    private String roleType;
    private SysUserEntity sysUserEntity;
    SysDepartmentService sysDepartmentService;

    public ProjectListener() {

    }

    /**
     * 不要使用自动装配
     * 在测试类中将dao当参数传进来
     */
    public ProjectListener(ProjectService projectService, ProjectUserService projectUserService,
                           SysUserService sysUserService,SysDepartmentService sysDepartmentService,
                           String excelMonth,String roleType,SysUserEntity sysUserEntity) {
        this.projectService = projectService;
        this.excelMonth = excelMonth;
        this.projectUserService = projectUserService;
        this.sysUserService = sysUserService;
        this.sysDepartmentService = sysDepartmentService;
        this.roleType = roleType;
        this.sysUserEntity = sysUserEntity;
    }

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(Project project, AnalysisContext context) {
        project.setExcelMonth(this.excelMonth);
        String userName = project.getUserName().trim().replaceAll(" ", "")
                .replaceAll("  ", "");
        if (StringUtils.isNotBlank(userName)) {
            if (userName.contains("\n")) {
                String[] userNames = userName.split("\n");
                userName = userNames[0];
            } else if (userName.contains(",")) {
                String[] userNames = userName.split(",");
                userName = userNames[0];
            } else if (userName.contains("，")) {
                String[] userNames = userName.split("，");
                userName = userNames[0];
            }
            Map<String, Object> params = new HashMap<>();
            params.put("username", userName);
            PageUtils page = sysUserService.queryPage(params);
            if (page.getList().size() > 0) {
                List<SysUserEntity> userList = (List<SysUserEntity>) page.getList();
                project.setUserId(userList.get(0).getUserId());
            }
            project.setUserName(userName);
        }
        project.setExcelUserId(sysUserEntity.getUserId());
        if("2".equals(roleType)){
            SysDepartmentEntity departmentEntity = sysDepartmentService.getById(sysUserEntity.getDepartmentId());
            project.setDepartmentName(departmentEntity.getDepartmentName());
            project.setDepartmentId(departmentEntity.getId());
        }else if("0".equals(roleType) || "4".equals(roleType)){
            String departmentName = project.getDepartmentName().trim()
                    .replaceAll("（牵头）", "").replaceAll("(牵头)", "");
            if (StringUtils.isNotBlank(departmentName)) {
                if (departmentName.contains("\n")) {
                    String[] departmentNames = departmentName.split("\n");
                    departmentName = departmentNames[0];
                } else if (userName.contains(",")) {
                    String[] departmentNames = departmentName.split(",");
                    departmentName = departmentNames[0];
                } else if (userName.contains("，")) {
                    String[] departmentNames = departmentName.split("，");
                    departmentName = departmentNames[0];
                }
                Map<String, Object> params = new HashMap<>();
                params.put("departmentName", departmentName);
                Map<String, Object> result = sysDepartmentService.getDepartmentByMap(params);
                List<SysDepartmentEntity> departList = (List<SysDepartmentEntity>) result.get("dataList");
                if (departList != null && departList.size() > 0) {
                    project.setDepartmentId(departList.get(0).getId());
                }
                project.setDepartmentName(departmentName);
            }
        }else {
            return;
        }

        project.setCreateTime(new Date());
        project.setModifiedTime(new Date());
        list.add(project);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();

    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        for (Project project : list) {

            projectService.save(project);
            if (project.getProjectUserNames() != null && project.getProjectUserNames().length() > 0) {
                String[] userNames = project.getProjectUserNames().split(",");
                for (String userName : userNames) {
                    userName = userName.trim().replaceAll(" ", "")
                            .replaceAll("  ", "");
                    if (userName != null && userName.length() > 0) {
                        SysUserEntity sysUserEntity = sysUserService.selectByUserName(userName);
                        if (sysUserEntity != null) {
                            ProjectUser projectUser = new ProjectUser();
                            projectUser.setProjectId(project.getId());
                            projectUser.setUserId(sysUserEntity.getUserId());
                            projectUser.setUt(new BigDecimal(0));
                            projectUser.setCreateTime(new Date());
                            projectUser.setModifiedTime(new Date());
                            projectUserService.save(projectUser);
                        }
                    }
                }
            }

        }
    }


}
