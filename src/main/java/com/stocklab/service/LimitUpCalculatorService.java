package com.stocklab.service;

import com.stocklab.domain.dto.LimitUpCalculateDTO;
import com.stocklab.domain.vo.LimitUpCalculateVO;

public interface LimitUpCalculatorService {

    LimitUpCalculateVO calculate(LimitUpCalculateDTO dto);
}
