package com.mongo.mymongo.controller;


import com.mongo.mymongo.domain.dto.QuantitativeKnTagAddDTO;
import com.mongo.mymongo.domain.dto.QuantitativeKnTagDelDTO;
import com.mongo.mymongo.domain.dto.QuantitativeKnTagQueryDTO;
import com.mongo.mymongo.domain.dto.QuantitativeKnTagShowQueryDTO;
import com.mongo.mymongo.domain.vo.QuantitativeKnTagListVO;
import com.mongo.mymongo.domain.vo.QuantitativeKnTagShowVO;
import com.mongo.mymongo.service.KnowledgeQuantitativeKnTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定量库知识标签管理
 *
 * @Author flm
 * @Date 2026/01/21 14:/33
 * @Version 1.0
 */
@RestController
@RequestMapping("/quantitative/kn/tag")
@RequiredArgsConstructor
public class KnowledgeQuantitativeKnTagController {
    private final KnowledgeQuantitativeKnTagService knowledgeQuantitativeKnTagService;


    /**
     * 查询标签列表
     *
     * @param dto
     * @return
     */
    @PostMapping("/list")
    public List<QuantitativeKnTagListVO> queryTags(@RequestBody QuantitativeKnTagQueryDTO dto) {
        return knowledgeQuantitativeKnTagService.queryTags(dto);
    }

    /**
     * 添加标签
     *
     * @param dto
     * @return
     */
    @PostMapping("/add")
    public Boolean addTag(@RequestBody QuantitativeKnTagAddDTO dto) {
        return knowledgeQuantitativeKnTagService.addTag(dto);
    }

    /**
     * 删除标签
     *
     * @param dto
     * @return
     */
    @DeleteMapping("/del")
    public Boolean delTag(@RequestBody QuantitativeKnTagDelDTO dto) {
        return knowledgeQuantitativeKnTagService.delTag(dto);
    }

    /**
     * 表关联关系标签展示
     *
     * @return
     */
    @PostMapping("/showTags")
    public QuantitativeKnTagShowVO showTags(@RequestBody QuantitativeKnTagShowQueryDTO dto) {
        return knowledgeQuantitativeKnTagService.showTags(dto);
    }

}
