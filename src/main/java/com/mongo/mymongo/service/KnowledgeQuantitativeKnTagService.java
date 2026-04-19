package com.mongo.mymongo.service;



import com.mongo.mymongo.domain.dto.QuantitativeKnTagAddDTO;
import com.mongo.mymongo.domain.dto.QuantitativeKnTagDelDTO;
import com.mongo.mymongo.domain.dto.QuantitativeKnTagQueryDTO;
import com.mongo.mymongo.domain.dto.QuantitativeKnTagShowQueryDTO;
import com.mongo.mymongo.domain.vo.QuantitativeKnTagListVO;
import com.mongo.mymongo.domain.vo.QuantitativeKnTagShowVO;

import java.util.List;

/**
 * @Description
 * @Author flm
 * @Date 2026/01/22 14:/40
 * @Version 1.0
 */
public interface KnowledgeQuantitativeKnTagService {
    List<QuantitativeKnTagListVO> queryTags(QuantitativeKnTagQueryDTO dto);

    Boolean addTag(QuantitativeKnTagAddDTO dto);

    Boolean delTag(QuantitativeKnTagDelDTO dto);

    QuantitativeKnTagShowVO showTags(QuantitativeKnTagShowQueryDTO dto);
}
