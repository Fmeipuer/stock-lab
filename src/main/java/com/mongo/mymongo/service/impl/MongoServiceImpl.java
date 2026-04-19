package com.mongo.mymongo.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.mongo.mymongo.domain.param.MongoAddParam;
import com.mongo.mymongo.domain.param.MongoDelParam;
import com.mongo.mymongo.domain.param.MongoEditParam;
import com.mongo.mymongo.domain.param.MongoListParam;
import com.mongo.mymongo.domain.po.DemoCollection;
import com.mongo.mymongo.domain.po.DemoCollectionInfo;
import com.mongo.mymongo.domain.vo.MongoAddVO;
import com.mongo.mymongo.domain.vo.MongoDelVO;
import com.mongo.mymongo.domain.vo.MongoEditVO;
import com.mongo.mymongo.domain.vo.MongoListVO;
import com.mongo.mymongo.service.MongoService;
import com.mongo.mymongo.util.PageResult;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Description
 * @Author flm
 * @Date 2025/10/10 09:/38
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MongoServiceImpl implements MongoService {

    private final MongoTemplate mongoTemplate;

    @Override
    public MongoAddVO add(MongoAddParam param) {
        Query query = Query.query(Criteria.where("lib_id").is(param.getLibId()));
        DemoCollection collection = mongoTemplate.findOne(query, DemoCollection.class);
        //创建实体
        DemoCollectionInfo info = new DemoCollectionInfo();
        info.setObjId(IdUtil.fastSimpleUUID());
        info.setObjName(param.getObjName());
        info.setObjContent(param.getObjContent());
        // 判断库内是否存在
        if (ObjUtil.isEmpty( collection)){
            // 不存在则创建
            param.setObjId(info.getObjId());

            DemoCollection coll = new DemoCollection();
            coll.setLibId(param.getLibId());
            coll.setInfo(CollUtil.newArrayList(info));
            DemoCollection insert = mongoTemplate.insert(coll);
            log.info("创建成功：{}", insert);
        }else {
            // 存在则更新
            Optional<DemoCollectionInfo> first = collection.getInfo().stream().filter(s -> StrUtil.isNotBlank(s.getObjId()) && s.getObjId().equals(param.getObjId())).findFirst();
            if (first.isPresent()){
                // 更新已有记录
                DemoCollectionInfo demoCollectionInfo = first.get();
                demoCollectionInfo.setObjName(param.getObjName());
                demoCollectionInfo.setObjContent(param.getObjContent());
            }else {
                // 新增
                collection.getInfo().add(info);
            }
            // 更新collection
            Update update = new Update();
            update.set("info", collection.getInfo());
            mongoTemplate.updateFirst(query, update, DemoCollection.class);
        }
        return MongoAddVO.builder()
                .objId(param.getObjId())
                .build();
    }

    @Override
    public MongoEditVO edit(MongoEditParam param) {
        //根据libId查找对应的collection，下的info根据objId修改
        Query query = Query.query(Criteria.where("lib_id").is(param.getLibId()));
        DemoCollection collection = mongoTemplate.findOne(query, DemoCollection.class);
        if (ObjUtil.isEmpty(collection)){
            throw new RuntimeException("未找到对应的collection");
        }
        Optional<DemoCollectionInfo> first = collection.getInfo().stream().filter(s -> StrUtil.isNotBlank(s.getObjId()) && s.getObjId().equals(param.getObjId())).findFirst();
        if (first.isPresent()){
            // 更新已有记录
            DemoCollectionInfo demoCollectionInfo = first.get();
            demoCollectionInfo.setObjName(param.getObjName());
            demoCollectionInfo.setObjContent(param.getObjContent());
            Update update = new Update();
            update.set("info", collection.getInfo());
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, DemoCollection.class);
            log.info("更新成功：{}", updateResult);
        }else {
            throw new RuntimeException("未找到对应的info");
        }
        return MongoEditVO
                .builder()
                .objId(param.getObjId())
                .build();
    }

    @Override
    public MongoDelVO del(MongoDelParam param) {
        return null;
    }

    @Override
    public PageResult<MongoListVO> list(MongoListParam param) {
        Query query = Query.query(Criteria.where("lib_id").is(param.getLibId()));
        return null;
    }
}
