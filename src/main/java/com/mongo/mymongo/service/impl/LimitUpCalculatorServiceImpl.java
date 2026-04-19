package com.mongo.mymongo.service.impl;

import com.mongo.mymongo.domain.dto.LimitUpCalculateDTO;
import com.mongo.mymongo.domain.vo.LimitUpCalculateVO;
import com.mongo.mymongo.domain.vo.LimitUpPeriodAmountVO;
import com.mongo.mymongo.exception.BusinessException;
import com.mongo.mymongo.service.LimitUpCalculatorService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 涨停复利计算服务实现
 */
@Service
public class LimitUpCalculatorServiceImpl implements LimitUpCalculatorService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final int MONEY_SCALE = 2;
    private static final int RATE_SCALE = 10;
    private static final int MAX_PERIODS = 10000;

    @Override
    public LimitUpCalculateVO calculate(LimitUpCalculateDTO dto) {
        validate(dto);

        BigDecimal startingAmount = dto.getStartingAmount().setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal targetAmount = dto.getTargetAmount().setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal rate = dto.getRate();
        BigDecimal rateDecimal = rate.divide(ONE_HUNDRED, RATE_SCALE, RoundingMode.HALF_UP);
        BigDecimal multiplier = BigDecimal.ONE.add(rateDecimal);

        BigDecimal currentAmount = startingAmount;
        List<LimitUpPeriodAmountVO> periods = new ArrayList<>();

        if (currentAmount.compareTo(targetAmount) < 0) {
            int period = 0;
            while (currentAmount.compareTo(targetAmount) < 0) {
                period++;
                if (period > MAX_PERIODS) {
                    throw new BusinessException("计算次数超过上限，请检查输入参数");
                }

                currentAmount = currentAmount.multiply(multiplier).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
                periods.add(new LimitUpPeriodAmountVO(period, currentAmount));
            }
        }

        LimitUpCalculateVO vo = new LimitUpCalculateVO();
        vo.setTotalPeriods(periods.size());
        vo.setStartingAmount(startingAmount);
        vo.setRate(rate);
        vo.setTargetAmount(targetAmount);
        vo.setFinalAmount(currentAmount);
        vo.setPeriods(periods);
        return vo;
    }

    private void validate(LimitUpCalculateDTO dto) {
        if (dto == null) {
            throw new BusinessException("请求参数不能为空");
        }
        if (dto.getStartingAmount() == null) {
            throw new BusinessException("起始金额不能为空");
        }
        if (dto.getStartingAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("起始金额必须大于0");
        }
        if (dto.getRate() == null) {
            throw new BusinessException("涨停利率不能为空");
        }
        if (dto.getRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("涨停利率必须大于0");
        }
        if (dto.getRate().compareTo(ONE_HUNDRED) > 0) {
            throw new BusinessException("涨停利率不能大于100");
        }
        if (dto.getTargetAmount() == null) {
            throw new BusinessException("目标金额不能为空");
        }
        if (dto.getTargetAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("目标金额必须大于0");
        }
    }
}
