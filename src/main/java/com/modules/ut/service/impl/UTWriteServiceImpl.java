package com.modules.ut.service.impl;

import com.modules.sys.entity.SysUserEntity;
import com.modules.sys.service.SysUserService;
import com.modules.ut.VO.UTWriteVO;
import com.modules.ut.dao.UTWriteDao;
import com.modules.ut.entity.UTWrite;
import com.modules.ut.service.UTWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ut填报表 服务实现类
 * </p>
 *
 * @author java
 * @since 2021-11-27
 */
@Service("utWriteService")
public class UTWriteServiceImpl extends ServiceImpl<UTWriteDao, UTWrite> implements UTWriteService {

    @Autowired
    private SysUserService sysUserService;
    @Override
    public Map<String, Object> writeList(Map<String, Object> queryMap) {
        int total=0;
        if(queryMap.get("current") !=null && queryMap.get("size") !=null){
            int current =(int)queryMap.get("current");
            int size =(int)queryMap.get("size");
            queryMap.put("start",(current-1)*size);
            queryMap.put("size",size);
            total = baseMapper.writeListCount(queryMap).intValue();
        }
        List<UTWriteVO> list = baseMapper.writeList(queryMap);
        if(total==0){
            total = list.size();
        }
        BigDecimal usedUT = new BigDecimal("0");
        for (UTWriteVO utWriteVO : list) {
            if(utWriteVO.getUt()!=null){
                usedUT = usedUT.add(utWriteVO.getUt());
            }
        }
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("msg", "success");
        returnMap.put("current", queryMap.get("current"));
        returnMap.put("total", total);
        returnMap.put("size", queryMap.get("size"));
        returnMap.put("usedUt", usedUT);
        returnMap.put("dataList", list);
        return returnMap;
    }
    @Override
    public Map<String, Object> writeProjectList(SysUserEntity user, Long current, Long size) {
        List<Map> list = baseMapper.writeProjectList(user.getUserId(), (current - 1) * size, size);
        Long total = baseMapper.projectCount(user.getUserId());
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("msg", "success");
        returnMap.put("current", current);
        returnMap.put("total", total);
        returnMap.put("code", 0);
        returnMap.put("size", list.size());
        returnMap.put("dataList", list);
        return returnMap;
    }
    @Override
    public Map<String, Object> utDataStatisticsUserDetailList(Map<String, Object> queryMap) {
        int total=0;
        if(queryMap.get("current") !=null && queryMap.get("size") !=null){
            int current =(int)queryMap.get("current");
            int size =(int)queryMap.get("size");
            queryMap.put("start",(current-1)*size);
            queryMap.put("size",size);
            total = baseMapper.writeListCount(queryMap).intValue();
        }
        List<Map> list = baseMapper.utDataStatisticsUserDetailList(queryMap);
        if(total==0){
            total = list.size();
        }

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("msg", "success");
        returnMap.put("current", queryMap.get("current"));
        returnMap.put("total", total);
        returnMap.put("size", queryMap.get("size"));
        returnMap.put("dataList", list);
        return returnMap;
    }
    @Override
    public Map<String, Object> utDataStatisticsProjectDetailList(Map<String, Object> queryMap) {
        int total=0;
        if(queryMap.get("current") !=null && queryMap.get("size") !=null){
            int current =(int)queryMap.get("current");
            int size =(int)queryMap.get("size");
            queryMap.put("start",(current-1)*size);
            queryMap.put("size",size);
            total = baseMapper.writeListCount(queryMap).intValue();
        }
        List<Map> list = baseMapper.utDataStatisticsProjectDetailList(queryMap);
        if(total==0){
            total = list.size();
        }

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("msg", "success");
        returnMap.put("current", queryMap.get("current"));
        returnMap.put("total", total);
        returnMap.put("size", queryMap.get("size"));
        returnMap.put("dataList", list);
        return returnMap;
    }


}
