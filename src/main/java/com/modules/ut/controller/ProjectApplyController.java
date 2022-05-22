package com.modules.ut.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.common.utils.Constant;
import com.common.utils.DateUtils;
import com.common.utils.MapUtils;
import com.common.utils.R;
import com.modules.sys.controller.AbstractController;
import com.modules.sys.entity.SysDepartmentEntity;
import com.modules.sys.entity.SysUserEntity;
import com.modules.sys.entity.SysUserRoleEntity;
import com.modules.sys.service.SysDepartmentService;
import com.modules.sys.service.SysUserRoleService;
import com.modules.ut.entity.*;
import com.modules.ut.service.*;
import com.modules.ut.util.UTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/projectApply")
public class ProjectApplyController extends AbstractController {
    @Autowired
    private ProjectApplyService projectApplyService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private ProjectApplyApprovalService projectApplyApprovalService;
    @Autowired
    private ProjectApplyUserService projectApplyUserService;
    @Autowired
    private SysDepartmentService sysDepartmentService;
    private Logger logger = LoggerFactory.getLogger(ProjectApplyController.class);



    /**
     * 查询项目立项列表
     */
    @PostMapping("/projectApplyList")
    public R list(@RequestBody Map<String, Object> params){
        SysUserEntity sysUserEntity = getUser();
        String status  = (String)params.get("status");
        String roleTyp = UTUtils.getRoleType(sysUserEntity);
        params.put("roleType",roleTyp);
        if("0".equals(roleTyp)){
            //管理员
            params.put("roleType","0");
        }else if("1".equals(roleTyp)){
            //项目经理查询自己所有申请的项目立项
            params.put("userId",sysUserEntity.getUserId());
        }else if("2".equals(roleTyp)){
            //部门管理员查询自己部门所有的待审批的
            params.put("departmentId",sysUserEntity.getDepartmentId());
            params.put("projectStatus",0);
        }else if("3".equals(roleTyp)){
            //所领导查询自己部门所有的待审批的
            params.put("departmentId",sysUserEntity.getDepartmentId());
            params.put("projectStatus",1);
        }else if("4".equals(roleTyp)){
            //计划财务部查询所有的
            if("1".equals(status)){
                params.put("projectStatus",2);
            }else if("2".equals(status)){
                params.put("projectStatus",4);
            }else {
                params.put("projectStatus",99999);
            }
        }else if("5".equals(roleTyp)){
            //院领导查询所有的
            if("1".equals(status)){
                params.put("projectStatus",3);
            }else if("2".equals(status)){
                params.put("projectStatus",4);
            }else {
                params.put("projectStatus",99999);
            }
        }else {
            //其他角色不允许查询
            return R.ok(new HashMap<String,Object>());
        }
        Map<String,Object> result = projectApplyService.getProjectByMap(params);
        return R.ok(result);
    }
    /**
     * 项目立项审批
     */
    @PostMapping("/projectApplyApproval")
    public R projectApplyApproval(@RequestBody Map<String, Object> params){
        String approvalType = (String)params.get("approvalType");
        String approvalOpinion = (String)params.get("approvalOpinion");
        String roleType = (String)params.get("roleType");
        List<Integer> ids = (List<Integer>)params.get("ids");
        List<String> codes = (List<String>)params.get("codes");

        if(ids!=null && ids.size()>0){
            SysUserEntity user =  getUser();
            Date nowDate = new Date();
            //项目立项列表
            List<ProjectApply> projectApplys = new ArrayList<>();
            //项目集合
            List<Project> projects = new ArrayList<>();
            //审批记录集合
            List<ProjectApplyApproval> projectApplyApprovals = new ArrayList<>();

            for(int i=0;i<ids.size();i++){
                int id = ids.get(i);
                //变更立项申请状态
                ProjectApply projectApply = projectApplyService.getById(id);
                projectApply.setModifiedTime(nowDate);
                if("1".equals(approvalType)){
                    projectApply.setProjectStatus(-1);
                }else if("0".equals(approvalType)){
                    if("0".equals(roleType)){
                        //管理员
                        if(projectApply.getProjectStatus()==2){
                            if(codes==null || codes.size()!=ids.size()){
                                return R.error("请填写第"+(i+1)+"个项目编码，否则无法审批");
                            }else {
                                String code = codes.get(i);
                                if(code==null || "".equals(code)){
                                    return R.error("请填写第"+(i+1)+"个项目编码，否则无法审批");
                                }else {
                                    projectApply.setCode(code);
                                }
                            }
                        }else if (projectApply.getProjectStatus() ==4){
                            return R.error("已审批完成，无需审批");
                        }
                        projectApply.setProjectStatus(projectApply.getProjectStatus()+1);
                    }else if("2".equals(roleType)){
                        projectApply.setProjectStatus(1);
                    }else if("3".equals(roleType)){
                        projectApply.setProjectStatus(2);
                    }else if("4".equals(roleType)){
                        projectApply.setProjectStatus(3);
                        if(codes==null || codes.size()!=ids.size()){
                            return R.error("未填写项目编码，无法审批");
                        }else {
                            String code = codes.get(i);
                            if(code==null || "".equals(code)){
                                return R.error("未填写项目编码，无法审批");
                            }else {
                                projectApply.setCode(code);
                            }
                        }
                    }else if("5".equals(roleType)){
                        projectApply.setProjectStatus(4);
                    }else {
                        return R.error("无权审批");
                    }
                }else {
                    continue;
                }
                projectApplys.add(projectApply);
                //审批完成后组装项目
                if(projectApply.getProjectStatus()==4){
                    Project project = new Project();
                    project.setCode(projectApply.getCode());
                    project.setName(projectApply.getName());
                    project.setExcelMonth(projectApply.getApplyMonth());
                    project.setStartTime(DateUtils.stringToDate(projectApply.getStartTime(),"yyyy-MM-dd"));
                    project.setEndTime(DateUtils.stringToDate(projectApply.getEndTime(),"yyyy-MM-dd"));
                    project.setDepartmentId(projectApply.getDepartmentId());
                    project.setDepartmentName(projectApply.getDepartmentName());
                    project.setUserId(projectApply.getUserId());
                    project.setUserName(projectApply.getUserName());
                    project.setUt(projectApply.getUt());
                    project.setAccording(projectApply.getAccording());
                    project.setLevel(projectApply.getLevel());
                    project.setProjectType(projectApply.getProjectType());
                    project.setProjectState("1");
                    project.setCreateTime(new Date());
                    project.setModifiedTime(new Date());
                    project.setProjectApplyId(projectApply.getId());
                    projects.add(project);
                }
                //审批记录
                ProjectApplyApproval projectApplyApproval = new ProjectApplyApproval();
                projectApplyApproval.setProjectApplyId(Long.parseLong(id+""));
                projectApplyApproval.setApprovalType(approvalType);
                projectApplyApproval.setApprovalOpinion(approvalOpinion);
                projectApplyApproval.setApprovalUserId(user.getUserId());
                projectApplyApproval.setApprovalUserName(user.getUsername());
                projectApplyApproval.setCreateTime(nowDate);
                projectApplyApproval.setModifiedTime(nowDate);
                projectApplyApprovals.add(projectApplyApproval);
            }
            //审批
            projectApplyService.updateBatchById(projectApplys);
            //审批记录
            projectApplyApprovalService.saveBatch(projectApplyApprovals);
            //审批完成的立项，插入项目表
            if(projects.size()>0){
                //保存项目
                projectService.saveBatch(projects);
                //新增项目成员
                for(Project project:projects){
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put("project_id", project.getProjectApplyId());
                    List<ProjectApplyUser> numberList = projectApplyUserService.listByMap(objectMap);
                    List<ProjectUser> projectUsers = new ArrayList<>();
                    for(ProjectApplyUser projectApplyUser:numberList){
                        if(project.getUserId().longValue()==projectApplyUser.getUserId().longValue()){
                            continue;
                        }
                        ProjectUser projectUser = new ProjectUser();
                        projectUser.setProjectId(project.getId());
                        projectUser.setUserId(projectApplyUser.getUserId());
                        projectUser.setUt(projectApplyUser.getUt());
                        projectUser.setCreateTime(new Date());
                        projectUser.setModifiedTime(new Date());
                        projectUsers.add(projectUser);
                    }
                    ProjectUser projectUser1 = new ProjectUser();
                    projectUser1.setProjectId(project.getId());
                    projectUser1.setUserId(project.getUserId());
                    projectUser1.setUt(new BigDecimal("0"));
                    projectUser1.setCreateTime(new Date());
                    projectUser1.setModifiedTime(new Date());
                    projectUsers.add(projectUser1);
                    projectUserService.saveBatch(projectUsers);
                }
            }
        }
        return R.ok();
    }
    /**
     * 修改立项申请
     */
    @PostMapping("/updateProjectApply")
    public R updateProject(@RequestBody ProjectApply projectApply) {
        if(projectApply.getId()==null || projectApply.getId()==0){
            SysUserEntity user =  getUser();
            projectApply.setUserId(user.getUserId());
            projectApply.setUserName(user.getUsername());
            projectApply.setDepartmentId(user.getDepartmentId());
            SysDepartmentEntity sysDepartment =sysDepartmentService.getById(user.getDepartmentId());
            projectApply.setDepartmentName(sysDepartment.getDepartmentName());
            Date nowDate = new Date();
            projectApply.setCreateTime(nowDate);
            projectApply.setModifiedTime(nowDate);
            projectApply.setApplyMonth(DateUtils.format(nowDate,"yyyy-MM"));
            projectApply.setProjectStatus(0);
            projectApplyService.save(projectApply);
            setNumbers(projectApply.getNumberIds(),projectApply.getId(),"0");
        }else {
            ProjectApply projectOld = projectApplyService.getById(projectApply.getId());
            projectOld.setName(projectApply.getName());
            projectOld.setStartTime(projectApply.getStartTime());
            projectOld.setEndTime(projectApply.getEndTime());
            projectOld.setContents(projectApply.getContents());
            projectOld.setUt(projectApply.getUt());
            projectOld.setExpectedResult(projectApply.getExpectedResult());
            projectOld.setAccording(projectApply.getAccording());
            projectOld.setLevel(projectApply.getLevel());
            projectOld.setProjectType(projectApply.getProjectType());
            projectOld.setProjectStatus(0);
            projectOld.setModifiedTime(new Date());
            projectApplyService.updateById(projectOld);
            setNumbers(projectApply.getNumberIds(),projectApply.getId(),"1");
        }

        return R.ok();
    }

    private void setNumbers(List<Long> numbers,Long projectId,String type){
        if(numbers!=null && numbers.size()>0){
            //删除历史
            if("1".equals(type)){
                projectApplyUserService.removeByMap(new MapUtils().put("project_id", projectId));
            }
            Map<Long,Long> map = new HashMap<>();
            List<ProjectApplyUser> list = new ArrayList<>();
            Date nowDate = new Date();
            for(Long number : numbers){
                //验证重复
                Long numLong = map.get(number);
                if(numLong==null){
                    map.put(numLong,numLong);
                }else {
                    continue;
                }
                ProjectApplyUser projectApplyUser = new ProjectApplyUser();
                projectApplyUser.setProjectId(projectId);
                projectApplyUser.setUserId(number);
                projectApplyUser.setUt(new BigDecimal(0));
                projectApplyUser.setCreateTime(nowDate);
                projectApplyUser.setModifiedTime(nowDate);
                list.add(projectApplyUser);
            }
            projectApplyUserService.saveBatch(list);
        }
    }
    /**
     * 查询部门列表
     */
    @PostMapping("/queryDepartmentList")
    public R queryDepartmentList() {
        SysUserEntity sysUserEntity = getUser();
        List<SysDepartmentEntity> dataList = new ArrayList<>();
        String roleTyp = UTUtils.getRoleType(sysUserEntity);
        //超级管理员，计财，人力，院领导查询所有的部门
        if("0".equals(roleTyp) || "4".equals(roleTyp) || "5".equals(roleTyp) || "6".equals(roleTyp)){
            dataList = sysDepartmentService.lambdaQuery().list();
        }else if("1".equals(roleTyp) || "2".equals(roleTyp) || "3".equals(roleTyp)){
            //项目负责人，部门管理员，所领导查询自己部门
            SysDepartmentEntity sysdepartment = sysDepartmentService.getById(
                    sysUserEntity.getDepartmentId());
            dataList.add(sysdepartment);
        }
        return R.ok().put("dataList",dataList);
    }
}
