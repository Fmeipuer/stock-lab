package com.mongo.mymongo.service;

import com.mongo.mymongo.domain.dto.LimitUpCalculateDTO;
import com.mongo.mymongo.domain.vo.LimitUpCalculateVO;

public interface LimitUpCalculatorService {

    LimitUpCalculateVO calculate(LimitUpCalculateDTO dto);
}
