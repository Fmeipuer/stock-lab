package com.mongo.mymongo.domain.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * @Description
 * @Author flm
 * @Date 2025/09/23 17:/21
 * @Version 1.0
 */
@Data
@Document(collection = "democoll")
public class DemoCollection {
    /* 主键*/
    @Id
    private String id;

    /* 知识库id*/
    @Field("lib_id")
    private Long libId;

    /* 元数据信息*/
    @Field("info")
    private List<DemoCollectionInfo> info;

}
