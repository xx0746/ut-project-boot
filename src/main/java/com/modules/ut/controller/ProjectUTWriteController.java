package com.modules.ut.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.utils.R;
import com.modules.sys.controller.AbstractController;
import com.modules.sys.entity.SysUserEntity;
import com.modules.sys.service.SysUserService;
import com.modules.ut.VO.UTWriteAddVO;
import com.modules.ut.VO.UTWriteVO;
import com.modules.ut.entity.UTWrite;
import com.modules.ut.service.WorkTypeService;
import com.modules.ut.service.UTWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * ut填报表 前端控制器
 * </p>
 *
 * @author java
 * @since 2021-11-27
 */
@RestController
@RequestMapping("/projectUTWrite")
public class ProjectUTWriteController extends AbstractController {
    @Autowired
    private UTWriteService UTWriteService; //UT填报的service

    @Autowired
    private WorkTypeService workTypeService;

    @Autowired
    private SysUserService sysUserService;
    /**
     *
     * @param current
     * @param size
     * @return
     */
    @PostMapping("/writeProjectList")
    public R writeProjectList(@RequestParam("current") Long current, @RequestParam("size") Long size) {
        SysUserEntity user = getUser();
        Map<String, Object> returnMap = UTWriteService.writeProjectList(user, current, size);
        return R.ok(returnMap);
    }
    /**
     *
     */
    @PostMapping("/writeList")
    public R writeList(@RequestBody Map<String, Object> params) {
        Long userId = getUserId();
        params.put("userId",userId);
        Map<String, Object> returnMap = UTWriteService.writeList( params);
        return R.ok(returnMap);
    }
    /**
     *
     */
    @PostMapping("/writeAdd")
    public R writeAdd(@RequestBody UTWriteAddVO utWriteAddVO) {
        QueryWrapper<UTWrite> utWriteQueryWrapper = new QueryWrapper<>();
        utWriteQueryWrapper.eq("write_date",utWriteAddVO.getWriteDate());
        utWriteQueryWrapper.eq("project_id", utWriteAddVO.getProjectId() );
        utWriteQueryWrapper.eq("user_id", getUserId());
        List<UTWrite> list = UTWriteService.list(utWriteQueryWrapper);
        List<Long> ids = new ArrayList<>();
        for(UTWrite write :list){
            int status = write.getStatus();
            ids.add(write.getId());
            if(status !=0){
                return R.error("项目经理已核对工时，无法操作");
            }
        }
        if(list.size()>0){
            UTWriteService.removeByIds(ids);
        }
        List<UTWriteVO> writes = utWriteAddVO.getDataList();
        if(writes==null || writes.size()==0){
            return R.error("至少添加一条工时");
        }
        SysUserEntity user = getUser();
        List<UTWrite> utWrites = new ArrayList<>();
        for(UTWriteVO utWriteVO : writes){
            UTWrite write = new UTWrite();
            write.setUserId(user.getUserId());
            write.setDepartmentId(user.getDepartmentId());
            write.setOneId(utWriteVO.getOneId());
            write.setTwoId(utWriteVO.getTwoId());
            write.setStatus(0);
            write.setProjectId(utWriteAddVO.getProjectId());
            write.setYearAndMonth(utWriteAddVO.getWriteDate().substring(0,7));
            write.setUt(utWriteVO.getUt());
            write.setWriteContent(utWriteVO.getWriteContent());
            write.setWriteDate(utWriteAddVO.getWriteDate());
            write.setCreateTime(new Date());
            write.setModifiedTime(new Date());
            utWrites.add(write);
        }
        UTWriteService.saveBatch(utWrites);
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("code", 0);
        returnMap.put("msg", "success");
        return R.ok(returnMap);
    }
    /**
     *
     */
    @PostMapping("/utDataStatisticsUserDetailList")
    public R utDataStatisticsUserDetailList(@RequestBody Map<String, Object> params) {
        String type = (String)params.get("type");
        Map<String, Object> returnMap = null;
        if("1".equals(type)){
            returnMap = UTWriteService.utDataStatisticsUserDetailList( params);
        }else if("2".equals(type)){
            returnMap = UTWriteService.utDataStatisticsProjectDetailList( params);
        }else {
            R.error("查询失败，请联系管理员");
        }
        return R.ok(returnMap);
    }


}

