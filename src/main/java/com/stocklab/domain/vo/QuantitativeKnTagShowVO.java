package com.stocklab.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表关联关系展示返回实体
 *
 * @Author flm
 * @Date 2026/02/03 09:/48
 * @Version 1.0
 */
@Data
public class QuantitativeKnTagShowVO implements Serializable {

    private static final long serialVersionUID = -1878577056608302476L;

    /**
     * 节点数据
     */
    private List<QuantitativeKnTagShowDataVO> data;

    /**
     * 连线数据
     */
    private List<QuantitativeKnTagShowLinkVO> links;
}
