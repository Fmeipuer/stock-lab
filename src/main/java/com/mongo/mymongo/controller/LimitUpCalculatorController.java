package com.mongo.mymongo.controller;

import com.mongo.mymongo.domain.dto.LimitUpCalculateDTO;
import com.mongo.mymongo.domain.vo.LimitUpCalculateVO;
import com.mongo.mymongo.service.LimitUpCalculatorService;
import com.mongo.mymongo.util.Result;
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
