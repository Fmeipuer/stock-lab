package com.mongo.mymongo.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 涨停复利计算结果
 */
@Data
public class LimitUpCalculateVO {

    /**
     * 总共需要多少期涨停
     */
    private Integer totalPeriods;

    /**
     * 起始金额
     */
    private BigDecimal startingAmount;

    /**
     * 涨停利率百分比
     */
    private BigDecimal rate;

    /**
     * 目标金额
     */
    private BigDecimal targetAmount;

    /**
     * 最终金额
     */
    private BigDecimal finalAmount;

    /**
     * 每次涨停后的金额
     */
    private List<LimitUpPeriodAmountVO> periods;
}
