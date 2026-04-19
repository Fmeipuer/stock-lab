package com.mongo.mymongo.domain.po;

import cn.hutool.core.collection.CollUtil;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author flm
 * @Date 2025/10/17 14:/11
 * @Version 1.0
 */
@Data
@Builder
public class AdsLmRetlLoanDaySum {
    private Integer distrAcctCnt;
    private String brandDesc;



    private static BigDecimal averageByDays(List<AdsLmRetlLoanDaySum> list,
                                            Function<AdsLmRetlLoanDaySum, BigDecimal> getter,
                                            int days) {
        if (CollUtil.isEmpty(list) || days <= 0) {
            return BigDecimal.ZERO;
        }
        // 计算总和（null 值安全）
        BigDecimal sum = list.stream()
                .map(getter)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 根据天数计算平均值
        return sum.divide(BigDecimal.valueOf(days), 6, RoundingMode.HALF_UP);
    }
    public static void main(String[] args) {
        test2();
    }
    static void test() {
        // 模拟数据
        List<AdsLmRetlLoanDaySum> list = Arrays.asList(
                AdsLmRetlLoanDaySum.builder().distrAcctCnt(100).build(),
                AdsLmRetlLoanDaySum.builder().distrAcctCnt(150).build(),
                AdsLmRetlLoanDaySum.builder().distrAcctCnt(120).build()
        );
        int days = 3;
        BigDecimal bigDecimal = averageByDays(list, item -> BigDecimal.valueOf(item.getDistrAcctCnt()), days);
        System.out.println("总额除以天数后的平均值 = " + bigDecimal);
    }
    static void test2() {
        // 模拟天数
        int currDays = 5;

        // 模拟数据
        List<AdsLmRetlLoanDaySum> list = List.of(
                AdsLmRetlLoanDaySum.builder().brandDesc("比亚迪").distrAcctCnt(10).build(),
                AdsLmRetlLoanDaySum.builder().brandDesc("比亚迪").distrAcctCnt(15).build(),
                AdsLmRetlLoanDaySum.builder().brandDesc("特斯拉").distrAcctCnt(20).build(),
                AdsLmRetlLoanDaySum.builder().brandDesc("特斯拉").distrAcctCnt(25).build(),
                AdsLmRetlLoanDaySum.builder().brandDesc("丰田").distrAcctCnt(30).build()

        );

        // ✅ 按品牌分组求平均值
        Map<String, BigDecimal> map = list.stream()
                .collect(Collectors.groupingBy(
                        AdsLmRetlLoanDaySum::getBrandDesc,
                        Collectors.collectingAndThen(
                                Collectors.mapping(
                                        u -> BigDecimal.valueOf(u.getDistrAcctCnt()),
                                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                                ),
                                sum -> sum.divide(BigDecimal.valueOf(currDays), 2, RoundingMode.HALF_UP)
                        )
                ));

        // 打印结果
        map.forEach((k, v) -> System.out.println(k + " 平均值: " + v));
    }
}
