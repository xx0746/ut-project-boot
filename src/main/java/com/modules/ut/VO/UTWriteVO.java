package com.modules.ut.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UTWriteVO {
    private Long oneId;
    private String oneName;
    private Long twoId;
    private String twoName;
    private BigDecimal ut;
    private Integer status;
    private String writeContent;
}
