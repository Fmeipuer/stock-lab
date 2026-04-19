package com.mongo.mymongo.domain.param;

import com.mongo.mymongo.util.PageQueryDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Author flm
 * @Date 2025/10/10 10:/03
 * @Version 1.0
 */
@Data
public class MongoListParam  extends PageQueryDTO implements Serializable {
    private static final long serialVersionUID = -3236231541244168639L;

    /**
     * 知识库id
     */
    @NotNull(message = "知识库id不能为空")
    private Long libId;
    /**
     * 实体Id
     */
    @NotBlank(message = "实体Id不能为空")
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
