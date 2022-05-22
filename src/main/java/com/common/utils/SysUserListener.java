package com.common.utils;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.modules.sys.dao.SysUserDao;
import com.modules.sys.entity.SysDepartmentEntity;
import com.modules.sys.entity.SysRoleEntity;
import com.modules.sys.entity.SysUserEntity;
import com.modules.sys.service.SysDepartmentService;
import com.modules.sys.service.SysRoleService;
import com.modules.sys.service.SysUserRoleService;
import com.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;


import java.util.*;

// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
public class SysUserListener extends AnalysisEventListener<SysUserEntity> {
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;

    /**
     * 这个集合用于接收 读取Excel文件得到的数据
     */
    List<SysUserEntity> list = new ArrayList<SysUserEntity>();

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private SysUserService sysUserService;
    private SysDepartmentService sysDepartmentService;
    private SysRoleService sysRoleService;

    public SysUserListener() {

    }

    /**
     *
     * 不要使用自动装配
     * 在测试类中将dao当参数传进来
     */
    public SysUserListener(SysUserService sysUserService, SysDepartmentService sysDepartmentService,SysRoleService sysRoleService) {
        this.sysUserService = sysUserService;
        this.sysDepartmentService = sysDepartmentService;
        this.sysRoleService = sysRoleService;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     */
    @Override
    public void invoke(SysUserEntity userEntity, AnalysisContext context) {
        userEntity.setCreateUserId((long)Constant.SUPER_ADMIN);
        String departmentName = userEntity.getDepartmentName();
        if(departmentName!=null && !departmentName.equals("")){
            Map<String,Object> params = new HashMap<>();
            params.put("departmentName",departmentName);
            Map<String,Object> result = sysDepartmentService.getDepartmentByMap(params);
            List<SysDepartmentEntity> departList = (List<SysDepartmentEntity>)result.get("dataList");
            if(departList!=null && departList.size()>0){
                userEntity.setDepartmentId(departList.get(0).getId());
            }
        }
        List<Long> roleIdList = new ArrayList<>();
        String roleName = userEntity.getRoleName();
        if(roleName!=null && !roleName.equals("")){
            String[] roleNames = roleName.split(",");
            for(String role : roleNames){
                List<SysRoleEntity> roleList = sysRoleService.list(new QueryWrapper<SysRoleEntity>().like("role_name", role));
                if(roleList.size()>0){
                    roleIdList.add(roleList.get(0).getRoleId());
                }
            }
        }
        if(roleIdList.size()>0){
            userEntity.setRoleIdList(roleIdList);
        }
        userEntity.setCreateTime(new Date());
        list.add(userEntity);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();

    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
//        (userId=null, username=admin, password=null, salt=null, email=root@renren.io, mobile=13612345678, status=null, roleIdList=null, createUserId=null, createTime=null, staff=1, level=A, roleName=null)
        for (SysUserEntity sysUserEntity : list) {
            if (!objecIsNull(sysUserEntity)) {
                sysUserEntity.setPassword(sysUserEntity.getMobile());
                sysUserEntity.setStatus(1);
                sysUserService.saveUser(sysUserEntity);
            } else {
                break;
            }
        }
    }
    public static boolean objecIsNull(SysUserEntity sysUserEntity){
        if (sysUserEntity == null) {
            return true;
        }
        if (StrUtil.isEmpty(sysUserEntity.getUsername())) {
            return true;
        }
       /* if (StrUtil.isEmpty(sysUserEntity.getMobile())) {
            return true;
        }
        if (StrUtil.isEmpty(sysUserEntity.getEmail())) {
            return true;
        }
        if (StrUtil.isEmpty(sysUserEntity.getStaff())) {
            return true;
        }*/
        if (StrUtil.isEmpty(sysUserEntity.getLevel())) {
            return true;
        }
        return false;
    }
}
