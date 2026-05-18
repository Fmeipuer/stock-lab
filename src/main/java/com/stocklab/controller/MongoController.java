package com.stocklab.controller;

import com.stocklab.domain.param.MongoAddParam;
import com.stocklab.domain.param.MongoDelParam;
import com.stocklab.domain.param.MongoEditParam;
import com.stocklab.domain.param.MongoListParam;
import com.stocklab.domain.vo.MongoAddVO;
import com.stocklab.domain.vo.MongoDelVO;
import com.stocklab.domain.vo.MongoEditVO;
import com.stocklab.domain.vo.MongoListVO;
import com.stocklab.service.MongoService;
import com.stocklab.util.PageResult;
import com.stocklab.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Mongo控制器
 * @Author flm
 * @Date 2025/10/10 09:/29
 * @Version 1.0
 */
@RestController
@RequestMapping("/mongo/")
@RequiredArgsConstructor
public class MongoController {
    private final MongoService mongoService;

    /**
     * 添加
     * @param param
     * @return
     */
    @PostMapping("add")
    public Result<MongoAddVO> add(@RequestBody MongoAddParam param) {
        return Result.success(mongoService.add(param));
    }
    /**
     * 修改
     * @param param
     * @return
     */
    @PutMapping("edit")
    public Result<MongoEditVO> edit(@RequestBody MongoEditParam param) {
        return Result.success(mongoService.edit(param));
    }

    /**
     * 删除
     * @param param
     * @return
     */
    @DeleteMapping("del")
    public Result<MongoDelVO> del(@RequestBody MongoDelParam param) {
        return Result.success(mongoService.del(param));
    }
    /**
     * 列表
     * @param param
     * @return
     */
    @GetMapping("list")
    public Result<PageResult<MongoListVO>> list(MongoListParam param) {
        return Result.success(mongoService.list(param));
    }
}
