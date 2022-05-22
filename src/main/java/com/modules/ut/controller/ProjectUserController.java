package com.modules.ut.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.utils.R;
import com.modules.sys.service.SysUserService;
import com.modules.ut.entity.ProjectUser;
import com.modules.ut.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author java
 * @since 2021-11-18
 */
@RestController
@RequestMapping("/projectUser")
public class ProjectUserController {

    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询项目成员接口 2.2
     * @param current
     * @param size
     * @param projectId
     * @return
     */
    @PostMapping("/personList")
    public R personList(Long current, Long size, Long projectId) {
        Map<String,Object> returnMap = projectUserService.personList(current, size, projectId);
        return R.ok(returnMap);
    }

    /**
     * 新增项目成员接口 2.3
     * @param ut
     * @param
     * @param projectId
     * @return
     */
    @PostMapping("/addPerson")
    public R addPerson(Double ut, Long userId, Long projectId) {

        List<ProjectUser> list = projectUserService.list(
                new QueryWrapper<ProjectUser>()
                        .eq("project_id", projectId)
                        .eq("user_id", userId));
        if(list.size()>0){
            return R.error("该项目已分配给该员工，不能重复添加");
        }
        ProjectUser projectUser = new ProjectUser();
        projectUser.setProjectId(projectId);
        projectUser.setUserId(userId);
        projectUser.setUt(new BigDecimal(ut));
        projectUser.setCreateTime(new Date());
        projectUser.setModifiedTime(new Date());
        projectUserService.save(projectUser);
        return R.ok();
    }
    /**
     * 新增项目成员接口 2.3
     * @param ut
     * @param
     * @param
     * @return
     */
    @PostMapping("/updatePerson")
    public R updatePerson(Double ut, Long id) {
        ProjectUser projectUser = projectUserService.getById(id);
        if(projectUser ==null){
            return R.error("请刷新页面重新修改");
        }
        projectUser.setUt(new BigDecimal(ut));
        projectUserService.updateById(projectUser);
        return R.ok();
    }
    /**
     * 删除项目成员接口  2.4
     * @return
     */
    @PostMapping("/deletePerson")
    public R deletePerson(Long id) {
        boolean result = projectUserService.removeById(id);
        return R.ok();
    }


}

