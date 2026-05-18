package com.stocklab.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 单期涨停后金额
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimitUpPeriodAmountVO {

    /**
     * 第几期
     */
    private Integer period;

    /**
     * 涨停后金额
     */
    private BigDecimal amount;
}
