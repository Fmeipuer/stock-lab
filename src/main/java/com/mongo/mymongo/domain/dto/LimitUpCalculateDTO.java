package com.mongo.mymongo.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 涨停复利计算请求参数
 */
@Data
public class LimitUpCalculateDTO {

    /**
     * 起始金额
     */
    private BigDecimal startingAmount;

    /**
     * 涨停利率，按百分比输入，例如 10 表示 10%
     */
    private BigDecimal rate;

    /**
     * 目标金额
     */
    private BigDecimal targetAmount;
}
