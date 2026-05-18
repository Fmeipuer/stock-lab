package com.stocklab.domain.po;

import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * @Description
 * @Author flm
 * @Date 2025/10/10 09:/30
 * @Version 1.0
 */
@Data
public class DemoCollectionInfo {

    /**
     * 实体Id
     */
    @Field("obj_id")
    private String objId;

    /**
     * 实体名称
     */
    @TextIndexed
    @Field("obj_name")
    private String objName;

    /**
     * 实体内容
     */
    @TextIndexed
    @Field("obj_content")
    private List<String> objContent;
}
