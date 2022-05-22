package com.modules.ut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modules.ut.dao.UserPerformanceDao;
import com.modules.ut.entity.UserPerformance;
import com.modules.ut.service.UserPerformanceService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service("UserPerformanceService")
public class UserPerformanceServiceImpl extends ServiceImpl<UserPerformanceDao, UserPerformance> implements UserPerformanceService {


}
