package com.stocklab.controller;

import com.stocklab.domain.dto.LimitUpCalculateDTO;
import com.stocklab.domain.vo.LimitUpCalculateVO;
import com.stocklab.service.LimitUpCalculatorService;
import com.stocklab.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 涨停复利计算控制器
 */
@RestController
@RequestMapping("/stock/limit-up")
@RequiredArgsConstructor
public class LimitUpCalculatorController {

    private final LimitUpCalculatorService limitUpCalculatorService;

    @PostMapping("/calculate")
    public Result<LimitUpCalculateVO> calculate(@RequestBody LimitUpCalculateDTO dto) {
        return Result.success(limitUpCalculatorService.calculate(dto));
    }
}
