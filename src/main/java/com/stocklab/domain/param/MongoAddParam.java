package com.stocklab.domain.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Author flm
 * @Date 2025/10/10 09:/46
 * @Version 1.0
 */
@Data
public class MongoAddParam implements Serializable {

    private static final long serialVersionUID = 7587752535506938161L;

    /**
     * 知识库id
     */
    private Long libId;
    /**
     * 实体Id
     */
    private String objId;
    /**
     * 实体名称
     */
    private String objName;
    /**
     * 实体内容
     */
    private List<String> objContent;

}
