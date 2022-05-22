package com.modules.ut.VO;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PerformanceVO  {
    @ExcelProperty(value = "月份", index = 0)
    private String yearAndMonth;
    @ExcelProperty(value = "员工部门", index = 1)
    @ColumnWidth(15)
    private String departmentName;
    @ExcelProperty(value = "员工姓名", index = 2)
    @ColumnWidth(12)
    private String username;
    @ExcelProperty(value = "综合绩效", index = 3)
    @ColumnWidth(12)
    private BigDecimal syntheticalPerformance;
    @ExcelProperty(value = "员工级别", index = 4)
    @ColumnWidth(12)
    private String level;
    @ExcelProperty(value = "员工系数", index = 5)
    @ColumnWidth(12)
    private BigDecimal userRatio;
    @ExcelProperty(value = "员工工时", index = 6)
    @ColumnWidth(12)
    private BigDecimal ut;
    @ExcelProperty(value = "工时单价", index = 7)
    @ColumnWidth(12)
    private BigDecimal utPrice;
    @ExcelProperty(value = "员工绩效", index = 8)
    @ColumnWidth(12)
    private BigDecimal performance;
}
