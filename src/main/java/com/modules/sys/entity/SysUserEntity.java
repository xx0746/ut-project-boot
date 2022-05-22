/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.modules.sys.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.common.validator.group.AddGroup;
import com.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	@TableId
	@ExcelIgnore
	private Long userId;

	/**
	 * 用户名
	 */
	@NotBlank(message="用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@ExcelProperty(value = "用户名", index = 0)
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message="密码不能为空", groups = AddGroup.class)
	@ExcelIgnore
	private String password;

	/**
	 * 盐
	 */
	@ExcelIgnore
	private String salt;

	/**
	 * 邮箱
	 */
	/*@NotBlank(message="邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Email(message="邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})*/
	@ExcelProperty(value = "邮箱", index = 1)
	private String email;

	/**
	 * 手机号
	 */
	@ExcelProperty(value = "手机号", index = 2)
	private String mobile;

	/**
	 * 状态  0：禁用   1：正常
	 */
	@ExcelIgnore
	private Integer status;

	/**
	 * 角色ID列表
	 */
	@TableField(exist=false)
	@ExcelIgnore
	private List<Long> roleIdList;

	/**
	 * 创建者ID
	 */
	@ExcelIgnore
	private Long createUserId;
	/**
	 * 部门ID
	 */
	@ExcelIgnore
	private Long departmentId;
	/**
	 * 部门名称
	 */
	@TableField(exist=false)
	@ExcelProperty(value = "部门名称", index = 5)
	private String departmentName;

	/**
	 * 创建时间
	 */
	@ExcelIgnore
	private Date createTime;

	/**
	 * 员工类别
	 */
	@ExcelProperty(value = "员工类别", index = 3)
	private String staff;

	/**
	* 层级
	 */
	@ExcelProperty(value = "层级", index = 4)
	private String level;

	/**
	 * 角色名
	 */
	@TableField(exist=false)
	/*@ExcelIgnore*/
	@ExcelProperty(value = "角色名", index = 6)
	private String roleName;




}
