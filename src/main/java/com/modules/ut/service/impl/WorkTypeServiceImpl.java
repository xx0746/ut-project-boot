package com.modules.ut.service.impl;

import com.modules.ut.dao.WorkTypeDao;
import com.modules.ut.entity.WorkType;
import com.modules.ut.service.WorkTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工作类型 服务实现类
 * </p>
 *
 * @author java
 * @since 2021-11-27
 */
@Service("WorkTypeService")
public class WorkTypeServiceImpl extends ServiceImpl<WorkTypeDao, WorkType> implements WorkTypeService {

}
