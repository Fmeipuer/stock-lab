package com.stocklab.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 表关联关系查询返回实体
 *
 * @Author flm
 * @Date 2026/01/22 14:/43
 * @Version 1.0
 */
@Data
public class QuantitativeKnTagListVO implements Serializable {

    private static final long serialVersionUID = -1878577056608302476L;

    /**
     * 源节点id
     */
    private String srcKnId;
    /**
     * 源表名称
     */
    private String srcTable;
    /**
     * 源节点描述
     */
    private String srcDesc;
    /**
     * 目标节点id
     */
    private String dstKnId;
    /**
     * 目标表名称
     */
    private String dstTable;
    /**
     * 目标节点描述
     */
    private String dstDesc;
    /**
     * 连接条件
     */
    private String joinCondition;
    /**
     * 关系描述
     */
    private String relationDesc;
}
