package com.modules.ut.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.utils.Constant;
import com.common.utils.R;
import com.modules.sys.controller.AbstractController;
import com.modules.sys.entity.SysUserEntity;
import com.modules.sys.service.SysUserRoleService;
import com.modules.ut.entity.UTWrite;
import com.modules.ut.entity.UTWriteManager;
import com.modules.ut.entity.UTWriteMonth;
import com.modules.ut.service.ApprovalService;
import com.modules.ut.service.UTWriteManagerService;
import com.modules.ut.service.UTWriteMonthService;
import com.modules.ut.service.UTWriteService;
import com.modules.ut.util.UTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/approval")
public class ApprovalController extends AbstractController {
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private UTWriteService UTWriteService;
    @Autowired
    private UTWriteMonthService utWriteMonthService;
    @Autowired
    private UTWriteManagerService UTWriteManagerService;
    private Logger logger = LoggerFactory.getLogger(ApprovalController.class);

    /**
     * 项目列表
     */
    @PostMapping("/projectApprovalList")
    public R projectApprovalList(@RequestBody Map<String, Object> params) {
        SysUserEntity user = getUser();
        if (user.getUserId() != (long) Constant.SUPER_ADMIN) {
            String roleTyp = UTUtils.getRoleType(user);
            if ("1".equals(roleTyp)) {
                params.put("userId", user.getUserId());
            } else if ("2".equals(roleTyp)) {
                params.put("departmentId", user.getDepartmentId());
            } else {
                R.error("无权查询");
            }
        }
        Map<String, Object> result = approvalService.projectApprovalList(params);
        return R.ok(result);
    }


    /**
     * 项目负责人审批工时列表
     */
    @PostMapping("/managerApprovalWorkTimeList")
    public R managerApprovalWorkTimeList(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = approvalService.managerApprovalWorkTimeList(params);
        return R.ok(result);
    }

    /**
     * 项目负责人工时审批
     */
    @PostMapping("/managerApprovalWorkTime")
    public R managerApprovalWorkTime(@RequestBody Map<String, Object> params) {
        List<Map> dataList = (List<Map>) params.get("dataList");
        if (dataList == null || dataList.size() == 0) {
            return R.error("请刷新页面后重试");
        }
        List<UTWrite> writeList = new ArrayList<>();
        List<UTWriteManager> utWriteManagers = new ArrayList<>();
        long projectId = ((Integer) params.get("projectId")).longValue();
        String yearAndMonth = (String) params.get("yearAndMonth");
        for (Map map : dataList) {
            UTWriteManager utWriteManager = new UTWriteManager();
            utWriteManager.setYearAndMonth(yearAndMonth);
            utWriteManager.setWriteDate((String) map.get("writeDate"));
            utWriteManager.setProjectId(projectId);
            utWriteManager.setUserId(Long.parseLong((String) map.get("userId")));
            utWriteManager.setDepartmentId(Long.parseLong((String) map.get("departmentId")));
            utWriteManager.setUt(new BigDecimal(String.valueOf(map.get("writeUT"))));
            utWriteManager.setStatus(0);
            utWriteManager.setCreateTime(new Date());
            utWriteManager.setModifiedTime(new Date());
            utWriteManagers.add(utWriteManager);
            List<Map> workTypelist = (List<Map>) map.get("workTypelist");
            if (workTypelist == null || workTypelist.size() == 0) {
                continue;
            }
            for (Map work : workTypelist) {
                String idStr = String.valueOf(work.get("id"));
                UTWrite write = UTWriteService.getById(Long.parseLong(idStr));
                write.setModifiedTime(new Date());
                write.setStatus(1);
                writeList.add(write);
            }

        }
        if (writeList.size() > 0) {
            UTWriteService.updateBatchById(writeList);
        }
        if (utWriteManagers.size() > 0) {
            UTWriteManagerService.saveBatch(utWriteManagers);
        }
        return R.ok();
    }

    /**
     * 领导 月总工时列表
     */
    @PostMapping("/utApprovalList")
    public R utApprovalList(@RequestBody Map<String, Object> params) {
        String status = String.valueOf(params.get("status"));
        SysUserEntity user = getUser();
        String roleTyp = UTUtils.getRoleType(user);
        if ("0".equals(roleTyp)) {
            params.put("status", "0,1,2");
        } else if ("3".equals(roleTyp)) {
            params.put("status", "0");
        } else if ("4".equals(roleTyp)) {
            params.put("status", "1");
        } else if ("6".equals(roleTyp)) {
            params.put("status", "2");
        } else {
            R.error("无权查询");
        }
        if ("2".equals(status)) {
            params.put("status", 3);
        }

        Map<String, Object> result = approvalService.utApprovalList(params);
        return R.ok(result);
    }

    /**
     * 领导 审批
     */
    @PostMapping("/utApproval")
    public R utApprova(@RequestBody Map<String, Object> params) {

        List<Map> dataList = (List<Map>) params.get("dataList");
        if (dataList == null || dataList.size() == 0) {
            return R.error("至少选择一条记录");
        }
        SysUserEntity user = getUser();
        String roleTyp = UTUtils.getRoleType(user);
        List<UTWriteMonth> utWriteMonths = new ArrayList<>();
        for (Map map : dataList) {
            List<Map> utList = (List<Map>) map.get("projectList");
            for (Map utMap : utList) {
                long id = Long.parseLong(String.valueOf(utMap.get("id")));
                UTWriteMonth utWriteMonth = utWriteMonthService.getById(id);
                if(utWriteMonth == null){
                    R.error("请刷新页面后重试");
                }
                BigDecimal newUT = new BigDecimal(String.valueOf(utMap.get("ut")));
                utWriteMonth.setUt(newUT);
                utWriteMonth.setModifiedTime(new Date());
                if ("0".equals(roleTyp)) {
                    int status = utWriteMonth.getStatus();
                    if(status<3){
                        utWriteMonth.setStatus(status+1);
                    }else {
                        return R.error("已经审批通过");
                    }
                } else if ("3".equals(roleTyp)) {
                    utWriteMonth.setStatus(1);
                } else if ("4".equals(roleTyp)) {
                    utWriteMonth.setStatus(2);
                } else if ("6".equals(roleTyp)) {
                    utWriteMonth.setStatus(3);
                }
                utWriteMonths.add(utWriteMonth);
            }
        }
        utWriteMonthService.updateBatchById(utWriteMonths);
        return R.ok();
    }
}
