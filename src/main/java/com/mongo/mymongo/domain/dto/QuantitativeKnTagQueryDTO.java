package com.mongo.mymongo.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 表关联关系查询实体
 *
 * @Author flm
 * @Date 2026/01/22 14:/44
 * @Version 1.0
 */
@Data
public class QuantitativeKnTagQueryDTO implements Serializable {

    private static final long serialVersionUID = -1878577056608302476L;
    /**
     * 知识库ID
     */
    @NotBlank(message = "知识库ID不能为空")
    private String libId;

}
