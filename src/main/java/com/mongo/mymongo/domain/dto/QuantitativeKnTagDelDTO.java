package com.mongo.mymongo.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 表关联关系删除实体
 *
 * @Author flm
 * @Date 2026/01/26 10:/19
 * @Version 1.0
 */
@Data
public class QuantitativeKnTagDelDTO implements Serializable {

    private static final long serialVersionUID = -1878577056608302476L;

    /**
     * 源节点id
     */
    @NotBlank(message = "源节点id不能为空")
    private String srcKnId;
    /**
     * 目标节点id
     */
    @NotBlank(message = "目标节点id不能为空")
    private String dstKnId;
}
