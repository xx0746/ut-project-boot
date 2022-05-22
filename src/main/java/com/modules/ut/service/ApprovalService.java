package com.modules.ut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modules.ut.entity.UTWrite;

import java.util.Map;

/**
 * <p>
 * 审批 服务类
 * </p>
 *
 * @author java
 * @since 2021-11-27
 */
public interface ApprovalService extends IService<UTWrite> {
    Map<String, Object> projectApprovalList(Map<String,Object> queryMap);
    Map<String, Object> managerApprovalWorkTimeList(Map<String,Object> queryMap);
    Map<String, Object> utApprovalList(Map<String,Object> queryMap);

}
