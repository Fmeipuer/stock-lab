package com.mongo.mymongo.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 表关联关系连线数据
 *
 * @Author flm
 * @Date 2026/02/03 10:/00
 * @Version 1.0
 */
@Data
public class QuantitativeKnTagShowLinkVO implements Serializable {

    private static final long serialVersionUID = -1878577056608302476L;

    /**
     * 来源节点ID
     */
    private String sourceId;

    /**
     * 目标节点ID
     */
    private String targetId;
    /**
     * 来源节点
     */
    private String sourceName;

    /**
     * 目标节点
     */
    private String targetName;
    /**
     * 连接条件
     */
    private String joinCondition;


}
