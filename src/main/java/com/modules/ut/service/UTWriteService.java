package com.modules.ut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modules.sys.entity.SysUserEntity;
import com.modules.ut.entity.UTWrite;

import java.util.Map;

/**
 * <p>
 * ut填报表 服务类
 * </p>
 *
 * @author java
 * @since 2021-11-27
 */
public interface UTWriteService extends IService<UTWrite> {
    Map<String, Object> writeList(Map<String, Object> params);
    Map<String, Object> writeProjectList(SysUserEntity user, Long current, Long size);
    Map<String, Object> utDataStatisticsUserDetailList(Map<String, Object> params);
    Map<String, Object> utDataStatisticsProjectDetailList(Map<String, Object> params);

}
