package com.stocklab.domain.po;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 图谱数据实体
 * @Author chenming
 * @Date 2025/10/24 14:/55
 * @Version 1.0
 */
@Data
public class GraphData implements Serializable {

    private static final long serialVersionUID = 7587752535506938161L;

    /**
     * 空间名称
     */
    private String spaceName;
    /**
     * 用时（微秒级）
     */
    private Long latencyInUs;
    /**
     * 列
     */
    private List<String> columns;
    /*
    * 行数据
     */
    private List<Row> data;
    @Data
    public static class Row {
        /**
         * 点集合
         */
        private List<JSONObject> meta;
        /**
         * 线集合
         */
        private List<String> row;
    }


}
