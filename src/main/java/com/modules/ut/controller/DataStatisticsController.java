package com.modules.ut.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.common.utils.Constant;
import com.common.utils.R;
import com.modules.sys.controller.AbstractController;
import com.modules.sys.entity.SysDepartmentEntity;
import com.modules.sys.entity.SysUserEntity;
import com.modules.sys.entity.SysUserRoleEntity;
import com.modules.sys.service.SysRoleService;
import com.modules.sys.service.SysUserRoleService;
import com.modules.sys.service.impl.SysDepartmentServiceImpl;
import com.modules.ut.VO.PerformanceVO;
import com.modules.ut.service.DataStatisticsService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dataStatistics")
public class DataStatisticsController extends AbstractController {
    @Autowired
    private DataStatisticsService dataStatisticsService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysDepartmentServiceImpl sysDepartmentService;
    @Autowired
    private SysRoleService sysRoleService;
    private Logger logger = LoggerFactory.getLogger(DataStatisticsController.class);

    /**
     * 员工统计列表
     */
    @PostMapping("/userWorkTimeList")
    public R userWorkTimeList(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = dataStatisticsService.userWorkTimeList(params);
        return R.ok(result);
    }


    /**
     * 项目统计列表
     */
    @PostMapping("/projectWorkTimeList")
    public R projectWorkTimeList(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = dataStatisticsService.projectWorkTimeList(params);
        return R.ok(result);
    }
    /**
     * 绩效列表
     */
    @PostMapping("/performanceList")
    public R performanceList(@RequestBody Map<String, Object> params){
        Map<String, Object> result = dataStatisticsService.performanceList(params);
        return R.ok(result);
    }
    /**
     * 绩效信息
     */
    @GetMapping("/exportPerformanceList")
    public void exportPerformanceList(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String year = request.getParameter("year");
        String yearAndMonth = request.getParameter("yearAndMonth");
        String departmentId = request.getParameter("departmentId");
        Map<String, Object> params = new HashMap<>();
        String fileStr = "";
        if(StringUtils.isNotBlank(year)){
            params.put("year",year);
            fileStr=year+"年";
        }
        if(StringUtils.isNotBlank(yearAndMonth)){
            params.put("yearAndMonth",yearAndMonth);
            fileStr=yearAndMonth+"月";
        }
        if(StringUtils.isNotBlank(departmentId) && !"null".equals(departmentId)){
            params.put("departmentId",departmentId);
        }
        Map<String, Object> result = dataStatisticsService.performanceList(params);
        List<PerformanceVO> list = (List<PerformanceVO>)result.get("dataList");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(fileStr+"绩效统计", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), PerformanceVO.class).sheet("列表").doWrite(list);


    }
}
