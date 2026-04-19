package com.mongo.mymongo.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 分页查询基础DTO
 *
 * @author system
 * @since 2025-07-17
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码（从1开始）
     */
    @Builder.Default
    private Long pageNum = 1L;

    /**
     * 每页显示条数
     */
    @Builder.Default
    private Long pageSize = 10L;


    public <T> Page<T> build() {
        Long pageNum = getPageNum();
        Long pageSize = getPageSize();
        if (pageNum <= 0) {
            pageNum = 1L;
        }
        Page<T> page = new Page<>(pageNum, pageSize);
        page.setOptimizeCountSql(false);
        return page;
    }

}
