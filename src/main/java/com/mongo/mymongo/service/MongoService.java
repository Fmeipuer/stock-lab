package com.mongo.mymongo.service;

import com.mongo.mymongo.domain.param.MongoAddParam;
import com.mongo.mymongo.domain.param.MongoDelParam;
import com.mongo.mymongo.domain.param.MongoEditParam;
import com.mongo.mymongo.domain.param.MongoListParam;
import com.mongo.mymongo.domain.vo.MongoAddVO;
import com.mongo.mymongo.domain.vo.MongoDelVO;
import com.mongo.mymongo.domain.vo.MongoEditVO;
import com.mongo.mymongo.domain.vo.MongoListVO;
import com.mongo.mymongo.util.PageResult;

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
