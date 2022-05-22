package com.modules.ut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modules.ut.dao.UTWriteMonthDao;
import com.modules.ut.entity.UTWriteMonth;
import com.modules.ut.service.UTWriteMonthService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("utWriteMonthService")
public class UTWriteMonthServiceImpl extends ServiceImpl<UTWriteMonthDao, UTWriteMonth> implements UTWriteMonthService {
    public List<UTWriteMonth> userManagerUtList(Map<String, Object> params) {
     return baseMapper.userManagerUtList(params);
    }
    public List<Map> userUtList(Map<String, Object> params) {
        return baseMapper.userUtList(params);
    }
}
