package com.modules.ut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modules.ut.VO.ProjectApplyUserVo;
import com.modules.ut.dao.ProjectApplyUserDao;
import com.modules.ut.entity.ProjectApplyUser;
import com.modules.ut.service.ProjectApplyUserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("projectApplyUserService")
public class ProjectApplyUserServiceImpl extends ServiceImpl<ProjectApplyUserDao, ProjectApplyUser> implements ProjectApplyUserService {

    @Override
    public Map<String, Object> queryProjectApplyUserList(Map<String, Object> queryMap) {
        int total =0;
        if(queryMap.get("current") !=null && queryMap.get("size") !=null){
            int current =(int)queryMap.get("current");
            int size =(int)queryMap.get("size");
            queryMap.put("start",(current-1)*size);
            queryMap.put("size",size);
            total = baseMapper.queryProjectApplyUserCount(queryMap).intValue();
        }
        List<ProjectApplyUserVo> list = baseMapper.queryProjectApplyUserList(queryMap);
        if(total==0){
            total = list.size();
        }

        Map<String,Object> map = new HashMap<>();
        map.put("current",queryMap.get("current"));
        map.put("size",queryMap.get("size"));
        map.put("total",total);
        map.put("dataList",list);
        return map;
    }
}
