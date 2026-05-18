package com.stocklab.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author flm
 * @Date 2025/10/10 10:/03
 * @Version 1.0
 */
@Data
@Builder
public class MongoEditVO implements Serializable {

    private static final long serialVersionUID = 7587752535506938161L;

    /**
     * 实体ID
     */
    private String objId;
}
