
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
 * 系统部门
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_department")
public class SysDepartmentEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 部门ID
	 */
	@TableId
	@ExcelIgnore
	private Long id;

	/**
	 * 部门名称
	 */

	@ExcelProperty(value = "部门名称", index = 0)
	private String departmentName;

	/**
	 * 创建时间
	 */
	@ExcelIgnore
	private Date createTime;
	/**
	 * 修改时间
	 */
	@ExcelIgnore
	private Date modifiedTime;



}
