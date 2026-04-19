package com.mongo.mymongo.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author flm
 * @Date 2025/10/10 09:/47
 * @Version 1.0
 */
@Data
@Builder
public class MongoAddVO implements Serializable {

    private static final long serialVersionUID = 7587752535506938161L;

    /**
     * 实体ID
     */
    private String objId;

}
