package com.mongo.mymongo.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 表关联关系节点数据
 *
 * @Author flm
 * @Date 2026/02/03 10:/00
 * @Version 1.0
 */
@Data
public class QuantitativeKnTagShowDataVO implements Serializable {

    private static final long serialVersionUID = -1878577056608302476L;

    /**
     * 标签id
     */
    private String tableId;

    /**
     * 标签名称
     */
    private String tableName;
}
