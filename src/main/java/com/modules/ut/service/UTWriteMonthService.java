package com.modules.ut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modules.ut.entity.UTWriteManager;
import com.modules.ut.entity.UTWriteMonth;

import java.util.List;
import java.util.Map;


public interface UTWriteMonthService extends IService<UTWriteMonth> {
    List<UTWriteMonth> userManagerUtList(Map<String, Object> params);
    List<Map> userUtList(Map<String, Object> params);
}
