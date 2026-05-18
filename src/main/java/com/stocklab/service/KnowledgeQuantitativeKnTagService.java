package com.stocklab.service;



import com.stocklab.domain.dto.QuantitativeKnTagAddDTO;
import com.stocklab.domain.dto.QuantitativeKnTagDelDTO;
import com.stocklab.domain.dto.QuantitativeKnTagQueryDTO;
import com.stocklab.domain.dto.QuantitativeKnTagShowQueryDTO;
import com.stocklab.domain.vo.QuantitativeKnTagListVO;
import com.stocklab.domain.vo.QuantitativeKnTagShowVO;

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
