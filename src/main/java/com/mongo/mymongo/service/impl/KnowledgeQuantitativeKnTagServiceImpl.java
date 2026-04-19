package com.mongo.mymongo.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.mongo.mymongo.domain.dto.QuantitativeKnTagAddDTO;
import com.mongo.mymongo.domain.dto.QuantitativeKnTagDelDTO;
import com.mongo.mymongo.domain.dto.QuantitativeKnTagQueryDTO;
import com.mongo.mymongo.domain.dto.QuantitativeKnTagShowQueryDTO;
import com.mongo.mymongo.domain.po.GraphData;
import com.mongo.mymongo.domain.vo.QuantitativeKnTagListVO;
import com.mongo.mymongo.domain.vo.QuantitativeKnTagShowDataVO;
import com.mongo.mymongo.domain.vo.QuantitativeKnTagShowLinkVO;
import com.mongo.mymongo.domain.vo.QuantitativeKnTagShowVO;
import com.mongo.mymongo.exception.BusinessException;
import com.mongo.mymongo.service.GraphExecutor;
import com.mongo.mymongo.service.KnowledgeQuantitativeKnTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

/**
 * 定量库知识标签服务
 *
 * @Author flm
 * @Date 2026/01/22 14:/41
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeQuantitativeKnTagServiceImpl implements KnowledgeQuantitativeKnTagService {

    private final GraphExecutor graphExecutor;
//    private final KnowledgeKnInfoMapper knowledgeKnInfoMapper;
    private final String SPACENAME = "table_relation_space";

    @Override
    public List<QuantitativeKnTagListVO> queryTags(QuantitativeKnTagQueryDTO dto) {

        // 1. 组装 nGQL
        String ngql = buildRelationNgql(dto.getLibId());

        // 2. 执行查询
        JSONObject executedJson = graphExecutor.executeJson(SPACENAME, ngql);

        // 3. 解析 ResultSet
        return parseNebulaResult(executedJson);
    }

    @Override
    public Boolean addTag(QuantitativeKnTagAddDTO dto) {
        if (relationExistsBothDirection(dto.getSrcKnId(), dto.getDstKnId())){
            throw new BusinessException("源节点和目标节点的关系已存在");
        }
        if (StrUtil.equals(dto.getSrcKnId(), dto.getDstKnId())){
            throw new BusinessException("源节点和目标节点不能相同");
        }
//        KnowledgeKnInfo srcKnInfo = knowledgeKnInfoMapper.selectById(dto.getSrcKnId());
//        if (ObjectUtil.isEmpty(srcKnInfo)) {
//            throw new BusinessException("源节点不存在：" + dto.getSrcKnId());
//        }
//        String srcTableName = srcKnInfo.getTableName();
//        String srcTitle = srcKnInfo.getTitle();
//        KnowledgeKnInfo dstKnInfo = knowledgeKnInfoMapper.selectById(dto.getDstKnId());
//        if (ObjectUtil.isEmpty(dstKnInfo)) {
//            throw new BusinessException("目标节点不存在：" + dto.getDstKnId());
//        }
//        String dstTableName = dstKnInfo.getTableName();
//        String dstTitle = dstKnInfo.getTitle();
        String srcTableName = "src"+dto.getSrcKnId()+"tableName";
        String srcTitle = "src"+dto.getSrcKnId()+"Title";
        String dstTableName = "dst"+dto.getDstKnId()+"tableName";
        String dstTitle = "dst"+dto.getDstKnId()+"Title";
        log.info("addTag: {}", dto);
        String nodeDdl = StrUtil.format("INSERT VERTEX table_tag(lib_id,table_id, table_name, `desc`) VALUES \"{}\": (\"{}\", \"{}\", \"{}\", \"{}\");",
                dto.getSrcKnId(),
                dto.getLibId(),
                StrUtil.isBlank(dto.getSrcKnId()) ? "" : dto.getSrcKnId(),
                StrUtil.isBlank(srcTableName) ? "" : srcTableName,
                StrUtil.isBlank(srcTitle) ? "" : srcTitle
        );
        log.info("nodeDdl: {}", nodeDdl);
        // 执行 nGQL 添加TAG
        graphExecutor.execute(SPACENAME, nodeDdl);
        if (StrUtil.isBlank(dto.getDstKnId())) {
            return true;
        }
        String targetNodeDdl = StrUtil.format("INSERT VERTEX table_tag(lib_id,table_id, table_name, `desc`) VALUES \"{}\": (\"{}\", \"{}\", \"{}\", \"{}\");",
                dto.getDstKnId(),
                dto.getLibId(),
                StrUtil.isBlank(dto.getDstKnId()) ? "" : dto.getDstKnId(),
                StrUtil.isBlank(dstTableName) ? "" : dstTableName,
                StrUtil.isBlank(dstTitle) ? "" : dstTitle
        );
        log.info("nodeDdl: {}", targetNodeDdl);
        // 执行 nGQL 添加TAG
        graphExecutor.execute(SPACENAME, targetNodeDdl);
        //添加边
        String edgeDdl = StrUtil.format("INSERT EDGE table_relation (join_condition)" +
                        "VALUES \"{}\" -> \"{}\":(\"{}\");",
                dto.getSrcKnId(),
                dto.getDstKnId(),
                StrUtil.isBlank(dto.getJoinCondition()) ? "" : dto.getJoinCondition()
        );
        log.info("edgeDdl: {}", edgeDdl);
        graphExecutor.execute(SPACENAME, edgeDdl);
        return true;
    }


    @Override
    public Boolean delTag(QuantitativeKnTagDelDTO dto) {
        String delDdl = StrUtil.format("DELETE EDGE table_relation \"{}\"-> \"{}\" ;", dto.getSrcKnId(), dto.getDstKnId());
        log.info("delDdl: {}", delDdl);
        graphExecutor.execute(SPACENAME, delDdl);
        // 2. 如果 src 没有任何关联边，则删除 src 节点
        if (!hasAnyRelation(dto.getSrcKnId())) {
            String delSrcVertex = StrUtil.format(
                    "DELETE VERTEX \"{}\";",
                    dto.getSrcKnId()
            );
            log.info("delSrcVertex: {}", delSrcVertex);
            graphExecutor.execute(SPACENAME, delSrcVertex);
        }

        // 3. 如果 dst 没有任何关联边，则删除 dst 节点
        if (!hasAnyRelation(dto.getDstKnId())) {
            String delDstVertex = StrUtil.format(
                    "DELETE VERTEX \"{}\";",
                    dto.getDstKnId()
            );
            log.info("delDstVertex: {}", delDstVertex);
            graphExecutor.execute(SPACENAME, delDstVertex);
        }
        return true;
    }

    @Override
    public QuantitativeKnTagShowVO showTags(QuantitativeKnTagShowQueryDTO dto) {
        String libId = dto.getLibId();
        QuantitativeKnTagShowVO quantitativeKnTagShowVO = new QuantitativeKnTagShowVO();
        // 查询点数据
        JSONObject nodeJsonObject = graphExecutor.executeJson(SPACENAME, "MATCH (v:table_tag)" +
                "WHERE properties(v).lib_id == \"" + libId + "\"\n" +
                " RETURN   properties(v).table_id AS tableId,properties(v).table_name AS tableName");
        GraphData graphNodeData = JSONUtil.toBean(nodeJsonObject, GraphData.class);
        // 查询边数据
        JSONObject edgeJsonObject = graphExecutor.executeJson(SPACENAME, "MATCH (s:table_tag)-[e:table_relation]->(t:table_tag)" +
                "WHERE properties(s).lib_id == \"" + libId + "\"\n" +
                "  AND properties(t).lib_id == \"" + libId + "\"\n" +
                " RETURN properties(s).table_id AS sourceId, properties(t).table_id AS targetId,properties(s).table_name AS sourceName, properties(t).table_name AS targetName,e.join_condition AS joinCondition");
        GraphData graphEdgeData = JSONUtil.toBean(edgeJsonObject, GraphData.class);
        // 构建返回体
        List<QuantitativeKnTagShowDataVO> quantitativeKnTagShowDataVOS = new ArrayList<>();
        List<QuantitativeKnTagShowLinkVO> quantitativeKnTagShowLinkVOS = new ArrayList<>();
        graphNodeData.getData().forEach(data -> {
            QuantitativeKnTagShowDataVO quantitativeKnTagShowDataVO = new QuantitativeKnTagShowDataVO();
            quantitativeKnTagShowDataVO.setTableId(data.getRow().get(0));
            quantitativeKnTagShowDataVO.setTableName(data.getRow().get(1));
            quantitativeKnTagShowDataVOS.add(quantitativeKnTagShowDataVO);
        });
        graphEdgeData.getData().forEach(data -> {
            QuantitativeKnTagShowLinkVO quantitativeKnTagShowLinkVO = new QuantitativeKnTagShowLinkVO();
            quantitativeKnTagShowLinkVO.setSourceId(data.getRow().get(0));
            quantitativeKnTagShowLinkVO.setTargetId(data.getRow().get(1));
            quantitativeKnTagShowLinkVO.setSourceName(data.getRow().get(2));
            quantitativeKnTagShowLinkVO.setTargetName(data.getRow().get(3));
            quantitativeKnTagShowLinkVO.setJoinCondition(data.getRow().get(4));
            quantitativeKnTagShowLinkVOS.add(quantitativeKnTagShowLinkVO);
        });

        quantitativeKnTagShowVO.setData(quantitativeKnTagShowDataVOS);
        quantitativeKnTagShowVO.setLinks(quantitativeKnTagShowLinkVOS);
        return quantitativeKnTagShowVO;
    }

    /**
     * 组装 nGQL
     *
     * @param libId
     * @return
     */
    private String buildRelationNgql(String libId) {

        return String.format(
                "MATCH (a:table_tag)-[r:table_relation]->(b:table_tag)\n" +
                        //"WHERE id(a) IN [%s]" +
                        "WHERE properties(a).lib_id == \"%s\"\n" +
                        "  AND properties(b).lib_id == \"%s\"\n" +
                        " RETURN\n" +
                        "  id(a) AS srcId,\n" +
                        "  a.table_tag.table_name AS srcTable,\n" +
                        "  a.table_tag.`desc` AS srcDesc,\n" +
                        "\n" +
                        "  id(b) AS dstId,\n" +
                        "  b.table_tag.table_name AS dstTable,\n" +
                        "  b.table_tag.`desc` AS dstDesc,\n" +
                        "\n" +
                        "  r.join_condition AS joinCondition,\n" +
                        "  r.`desc` AS relationDesc;\n",
                libId,
                libId
        );
    }

    /**
     * 解析 Nebula Graph 查询结果
     *
     * @param result
     * @return
     */
    public static List<QuantitativeKnTagListVO> parseNebulaResult(JSONObject result) {

        if (ObjectUtil.isNull(result)) {
            return Collections.emptyList();
        }
        // 获取列
        JSONArray columns = result.getJSONArray("columns");
        // 获取数据
        JSONArray data = result.getJSONArray("data");

        if (CollUtil.isEmpty(columns) || CollUtil.isEmpty(data)) {
            return Collections.emptyList();
        }

        Map<String, Integer> columnIndex = buildColumnIndex(columns);

        List<QuantitativeKnTagListVO> list = new ArrayList<>(data.size());
        for (int i = 0; i < data.size(); i++) {
            JSONArray row = data.getJSONObject(i).getJSONArray("row");
            if (CollUtil.isEmpty(row)) {
                continue;
            }
            list.add(buildVO(row, columnIndex));
        }
        return list;
    }

    /**
     * 构建列索引
     *
     * @param columns
     * @return
     */
    private static Map<String, Integer> buildColumnIndex(JSONArray columns) {
        Map<String, Integer> map = new HashMap<>(columns.size());
        IntStream.range(0, columns.size())
                .forEach(i -> map.put(columns.getStr(i), i));
        return map;
    }

    /**
     * 构建 VO
     *
     * @param row
     * @param idx
     * @return
     */
    private static QuantitativeKnTagListVO buildVO(JSONArray row, Map<String, Integer> idx) {
        QuantitativeKnTagListVO vo = new QuantitativeKnTagListVO();

        vo.setSrcKnId(get(row, idx, "srcId"));
        vo.setSrcTable(get(row, idx, "srcTable"));
        vo.setSrcDesc(get(row, idx, "srcDesc"));

        vo.setDstKnId(get(row, idx, "dstId"));
        vo.setDstTable(get(row, idx, "dstTable"));
        vo.setDstDesc(get(row, idx, "dstDesc"));

        vo.setJoinCondition(get(row, idx, "joinCondition"));
        vo.setRelationDesc(get(row, idx, "relationDesc"));

        return vo;
    }

    /**
     * 获取列值
     *
     * @param row
     * @param idx
     * @param column
     * @return
     */
    private static String get(JSONArray row, Map<String, Integer> idx, String column) {

        Integer i = idx.get(column);
        if (ObjectUtil.isNull(i) || i >= row.size()) {
            return null;
        }

        Object val = row.get(i);
        return ObjectUtil.isNull(val) ? null : String.valueOf(val);
    }

    private boolean relationExistsBothDirection(String srcId, String dstId) {
        // A -> B
        String forwardNgql = StrUtil.format(
                "FETCH PROP ON table_relation \"{}\"  ->   \"{}\" YIELD edge AS e;",
                srcId,
                dstId
        );
        JSONObject jsonObject = graphExecutor.executeJson(SPACENAME, forwardNgql);

        if (ObjectUtil.isNotEmpty(jsonObject) && ObjectUtil.isNotEmpty(jsonObject.get("data"))) {
            return true;
        }

        // B -> A
        String reverseNgql = StrUtil.format(
                "FETCH PROP ON table_relation \"{}\"  ->   \"{}\" YIELD edge AS e;",
                dstId,
                srcId
        );
        JSONObject executeJson = graphExecutor.executeJson(SPACENAME, reverseNgql);
        if (ObjectUtil.isNotEmpty(executeJson) && ObjectUtil.isNotEmpty(executeJson.get("data"))) {
            return true;
        }
        return false;
    }

    /**
     * 源节点或目标节点是否有关联关系
     *
     * @param vertexId
     * @return
     */
    private boolean hasAnyRelation(String vertexId) {
        String ngql = StrUtil.format(
                "GO FROM \"{}\" OVER table_relation " +
                        "YIELD dst(edge) | LIMIT 1;",
                vertexId
        );

        JSONObject result = graphExecutor.executeJson(SPACENAME, ngql);
        return ObjectUtil.isNotEmpty(result)
                && ObjectUtil.isNotEmpty(result.get("data"));
    }


}
