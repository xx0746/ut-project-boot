package com.modules.ut.controller;


import com.common.utils.R;
import com.modules.ut.VO.WorkTypeVO;
import com.modules.ut.entity.WorkType;
import com.modules.ut.service.WorkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 工作类型 前端控制器
 * </p>
 *
 * @author java
 * @since 2021-11-27
 */
@RestController
@RequestMapping("/workType")
public class WorkTypeController {
    @Autowired
    private WorkTypeService workTypeService;
    /**
     * 3.3
     */
    @PostMapping("getWorkTypeList")
    public R getWorkTypeList() {
        List<WorkType> workTypes = workTypeService.list();
        List<Map> dataList = new ArrayList<>();
        Map<String,List<Map>> resultMap = new HashMap();
        for(WorkType workType:workTypes){
            Long parentId= workType.getParentId();
            Map map = new HashMap();
            map.put("id",workType.getId());
            map.put("workType",workType.getName());
            if(parentId == null){
                dataList.add(map);
            }else {
                List<Map> list = resultMap.get(workType.getParentId()+"");
                if(list == null){
                    list = new ArrayList<>();
                }
                list.add(map);
                resultMap.put(workType.getParentId()+"",list);
            }
        }

        for(Map map :dataList){
            List<Map> list = resultMap.get(map.get("id")+"");
            if(list == null){
                list = new ArrayList<>();
            }
            map.put("children",list);
        }

        return R.ok().put("dataList",dataList);
    }
}

