package com.modules.ut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modules.ut.dao.ApprovalDao;
import com.modules.ut.entity.UTWrite;
import com.modules.ut.service.ApprovalService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批 服务实现类
 * </p>
 *
 * @author java
 * @since 2021-11-27
 */
@Service("ApprovalService")
public class ApprovalServiceImpl extends ServiceImpl<ApprovalDao, UTWrite> implements ApprovalService {
    @Override
    public Map<String, Object> projectApprovalList(Map<String, Object> queryMap) {
        int current = (int) queryMap.get("current");
        int size = (int) queryMap.get("size");
        queryMap.put("start", (current - 1) * size);
        queryMap.put("size", size);
        List<Map> list = baseMapper.projectApprovalList(queryMap);
        long total = baseMapper.projectApprovalCount(queryMap);
        int countAll = 0;
        for(Map map : list){
            int count = ((BigDecimal)map.get("noApprovalCount")).intValue();
            if(count>0){
                countAll = count;
                break;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("current", queryMap.get("current"));
        map.put("size", queryMap.get("size"));
        if(countAll>0){
            map.put("isShow", "1");
        }else {
            map.put("isShow", "0");
        }
        map.put("total", total);
        map.put("dataList", list);
        return map;
    }



    @Override
    public Map<String, Object> managerApprovalWorkTimeList(Map<String, Object> queryMap) {
        if("1".equals(String.valueOf(queryMap.get("queryType")))){
            queryMap.put("status","1");
        }else if("2".equals(String.valueOf(queryMap.get("queryType")))){
            queryMap.put("status","0");
        }else {
            return new HashMap<>();
        }
        List<Map> list = baseMapper.managerApprovalWorkTimeList(queryMap);
        Map<String,Object> result = new HashMap();
        for(Map map : list){
            String userId = String.valueOf(map.get("userId"));
            String username = (String)map.get("username");
            String writeDate = (String)map.get("writeDate");
            BigDecimal writeUT = (BigDecimal)map.get("writeUT");
            BigDecimal writeUTBig = (BigDecimal)result.get(userId+writeDate+"writeUT");
            if(writeUTBig == null){
                result.put(userId+writeDate+"writeUT",writeUT);
            }
            List<Map> maps = (List<Map>)result.get(userId+":"+username+":"+writeDate+":"+map.get("departmentId"));
            BigDecimal workTime = (BigDecimal)result.get(userId+writeDate+"workTime");
            if(maps ==null){
                maps = new ArrayList<>();
            }
            if(workTime == null){
                workTime = new BigDecimal("0");
            }
            BigDecimal ut = (BigDecimal)map.get("ut");
            if(ut == null){
                ut=new BigDecimal("0");
            }
            maps.add(map);
            result.put(userId+":"+username+":"+writeDate+":"+map.get("departmentId"),maps);
            result.put(userId+writeDate+"workTime",workTime.add(ut));
        }
        List<Map> resultList = new ArrayList<>();
        for(String key:result.keySet()){
            if(key.contains("workTime") || key.contains("writeUT")){
                continue;
            }
            String[] keys = key.split(":");
            List<Map> workTypelist = (List<Map>)result.get(key);
            Map map = new HashMap();
            map.put("workTypelist",workTypelist);
            map.put("writeUT",result.get(keys[0]+keys[2]+"writeUT"));
            map.put("userId",keys[0]);
            map.put("username",keys[1]);
            map.put("writeDate",keys[2]);
            map.put("departmentId",keys[3]);
            map.put("workTime",result.get(keys[0]+keys[2]+"workTime"));
            resultList.add(map);
        }
        Collections.sort(resultList, (o1, o2) -> {
            String username1= o1.get("username").toString() ;
            String username2= o2.get("username").toString();
            if(username1.equals(username2)){
                String writeDate1= o1.get("writeDate").toString() ;
                String writeDate2= o2.get("writeDate").toString();
                return writeDate2.compareTo(writeDate1);
            }else {
                return username1.compareTo(username2);   //return age2.compareTo(age1);为降序
            }
        });
        Map<String, Object> map = new HashMap<>();
        map.put("dataList", resultList);
        return map;
    }


    @Override
    public Map<String, Object> utApprovalList(Map<String, Object> queryMap) {
        int current = (int) queryMap.get("current");
        int size = (int) queryMap.get("size");
        queryMap.put("start", (current - 1) * size);
        queryMap.put("size", size);
        List<Map> list = baseMapper.utApprovalList(queryMap);
        long total = baseMapper.utApprovalCount(queryMap);
        String userIds = "";
        for(Map map : list){
            if(userIds.equals("")){
                userIds = String.valueOf(map.get("userId"));
            }else {
                userIds += "," +map.get("userId");
            }
        }
        if(!userIds.equals("")){
            queryMap.put("userIds",userIds);
            List<Map> projectList = baseMapper.utApprovalProjectList(queryMap);
            for(Map map : list){
                String userId = String.valueOf(map.get("userId"));
                for(Map projectMap : projectList){
                    String projectUserId = String.valueOf(projectMap.get("userId"));
                    if(userId.equals(projectUserId)){
                        List<Map> projects = (List<Map>)map.get("projectList");
                        if(projects == null){
                            projects = new ArrayList<>();
                        }
                        projects.add(projectMap);
                        map.put("projectList",projects);
                    }
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("current", queryMap.get("current"));
        map.put("size", queryMap.get("size"));
        map.put("total", total);
        map.put("dataList", list);
        return map;
    }
}
