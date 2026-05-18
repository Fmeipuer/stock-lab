package com.stocklab.service;

import com.stocklab.domain.param.MongoAddParam;
import com.stocklab.domain.param.MongoDelParam;
import com.stocklab.domain.param.MongoEditParam;
import com.stocklab.domain.param.MongoListParam;
import com.stocklab.domain.vo.MongoAddVO;
import com.stocklab.domain.vo.MongoDelVO;
import com.stocklab.domain.vo.MongoEditVO;
import com.stocklab.domain.vo.MongoListVO;
import com.stocklab.util.PageResult;

/**
 * @Description
 * @Author flm
 * @Date 2025/10/10 09:/29
 * @Version 1.0
 */
public interface MongoService {
    MongoAddVO add(MongoAddParam param);

    MongoEditVO edit(MongoEditParam param);

    MongoDelVO del(MongoDelParam param);

    PageResult<MongoListVO> list(MongoListParam param);
}
