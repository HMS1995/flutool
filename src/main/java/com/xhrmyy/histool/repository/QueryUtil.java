package com.xhrmyy.histool.repository;

import com.xhrmyy.histool.entity.QueueInfo;
import com.xhrmyy.histool.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class QueryUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(QueryUtil.class);

    /**
     * 获取用药记录数据
     * @return
     */
    public List<PDR> getPDRInfo() {

        String sql = "select distinct 就诊类型           as P7501,\n" +
                "                就诊卡号           as P7502,\n" +
                "                就诊日期           as P7506,\n" +
                "                顺序号             as P7500,\n" +
                "                药物名称           as P8016,\n" +
                "                药物使用频率日次数 as P8017,\n" +
                "                药物使用总剂量     as P8018,\n" +
                "                药物使用次剂量     as P8019,\n" +
                "                药物使用剂量单位   as P8020,\n" +
                "                药物使用开始时间   as P8021,\n" +
                "                药物使用结束时间   as P8022\n" +
                "  from (select '01' as 就诊类型,\n" +
                "               a.门诊号 as 就诊卡号,\n" +
                "               to_char(b.记录日期, 'yyyy-mm-dd hh24:mi:ss') as 就诊日期,\n" +
                "               c.id as 顺序号,\n" +
                "               c.医嘱内容 as 药物名称,\n" +
                "               nvl(c.频率次数, 1) as 药物使用频率日次数,\n" +
                "               round(nvl(c.总给予量, 1) * c.单次用量, 4) as 药物使用总剂量,\n" +
                "               round(c.单次用量, 4) as 药物使用次剂量,\n" +
                "               e.计算单位 as 药物使用剂量单位,\n" +
                "               to_char(c.开始执行时间, 'yyyy-mm-dd hh24:mi:ss') as 药物使用开始时间,\n" +
                "               null as 药物使用结束时间\n" +
                "          from 病人信息     a,\n" +
                "               病人诊断记录 b,\n" +
                "               病人医嘱记录 c,\n" +
                "               病人挂号记录 d,\n" +
                "               诊疗项目目录 e\n" +
                "         where a.病人id = b.病人id\n" +
                "           and b.病人id = c.病人id\n" +
                "           and b.主页id = d.id\n" +
                "           and c.挂号单 = d.no\n" +
                "           and c.诊疗项目id = e.id\n" +
                "           and e.类别 in ('5', '6', '7')\n" +
                "           and c.单次用量 is not null\n" +
                "           and (REGEXP_LIKE(substr(b.诊断描述, 2, 3), '^J\\d\\d$') OR b.诊断描述 like '%流%感' OR b.诊断描述 like '甲%流' OR b.诊断描述 like '乙%流')\n" +
                "           and b.记录日期 >=trunc(sysdate-7,'DD')\n" +
                "        union all\n" +
                "        select '03' as 就诊类型,\n" +
                "               a.住院号 as 就诊卡号,\n" +
                "               to_char(d.入院日期, 'yyyy-mm-dd hh24:mi:ss') as 就诊日期,\n" +
                "               c.id as 顺序号,\n" +
                "               c.医嘱内容 as 药物名称,\n" +
                "               nvl(c.频率次数, 1) as 药物使用频率日次数,\n" +
                "               round(nvl(c.总给予量, 1) * c.单次用量, 4) as 药物使用总剂量,\n" +
                "               round(c.单次用量, 4) as 药物使用次剂量,\n" +
                "               e.计算单位 as 药物使用剂量单位,\n" +
                "               to_char(c.开始执行时间, 'yyyy-mm-dd hh24:mi:ss') as 药物使用开始时间,\n" +
                "               null as 药物使用结束时间\n" +
                "          from 病人信息     a,\n" +
                "               病人诊断记录 b,\n" +
                "               病人医嘱记录 c,\n" +
                "               病案主页     d,\n" +
                "               诊疗项目目录 e\n" +
                "         where a.病人id = b.病人id\n" +
                "           and b.病人id = c.病人id\n" +
                "           and b.病人id = d.病人id\n" +
                "           and b.主页id = d.主页id\n" +
                "           and c.诊疗项目id = e.id\n" +
                "           and e.类别 in ('5', '6', '7')\n" +
                "           and c.单次用量 is not null\n" +
                "           and (REGEXP_LIKE(substr(b.诊断描述, 2, 3), '^J\\d\\d$') OR b.诊断描述 like '%流%感' OR b.诊断描述 like '甲%流' OR b.诊断描述 like '乙%流')\n" +
                "           and (d.出院日期 is null or\n" +
                "               d.出院日期 >=trunc(sysdate-7,'DD'))\n" +
                "           and c.开始执行时间 <= sysdate\n" +
                "         order by 就诊卡号, 顺序号)\n";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PDR.class));
    }

    /**
     * 获取门急诊和在院流感病例数据
     * @return
     */
    public List<FLU> getFLUInfo() {

        String sql = "select distinct '4484272059' P900,\n" +
                "                '新晃侗族自治县人民医院' P6891,\n" +
                "                医疗保险手册 as P686,\n" +
                "                健康卡号 as P800,\n" +
                "                就诊类型 as P7501,\n" +
                "                就诊卡号 as P7502,\n" +
                "                姓名 as P4,\n" +
                "                性别 as P5,\n" +
                "                出生日期 as P6,\n" +
                "                年龄 as P7,\n" +
                "                注册证件类型代码 as P7503,\n" +
                "                注册证件号码 as P13,\n" +
                "                就诊科室代码 as P7504,\n" +
                "                就诊次数 as P7505,\n" +
                "                就诊日期 as P7506,\n" +
                "                主诉 as P7507,\n" +
                "                主要疾病诊断代码 as P321,\n" +
                "                主要疾病诊断描述 as P322,\n" +
                "                其他疾病诊断代码1 as P324,\n" +
                "                其他疾病诊断描述1 as P325,\n" +
                "                其他疾病诊断代码2 as P327,\n" +
                "                其他疾病诊断描述2 as P328,\n" +
                "                其他疾病诊断代码3 as P3291,\n" +
                "                其他疾病诊断描述3 as P3292,\n" +
                "                其他疾病诊断代码4 as P3294,\n" +
                "                其他疾病诊断描述4 as P3295,\n" +
                "                其他疾病诊断代码5 as P3297,\n" +
                "                其他疾病诊断描述5 as P3298,\n" +
                "                其他疾病诊断代码6 as P3281,\n" +
                "                其他疾病诊断描述6 as P3282,\n" +
                "                其他疾病诊断代码7 as P3284,\n" +
                "                其他疾病诊断描述7 as P3285,\n" +
                "                其他疾病诊断代码8 as P3287,\n" +
                "                其他疾病诊断描述8 as P3288,\n" +
                "                其他疾病诊断代码9 as P3271,\n" +
                "                其他疾病诊断描述9 as P3272,\n" +
                "                其他疾病诊断代码10 as P3274,\n" +
                "                其他疾病诊断描述10 as P3275,\n" +
                "                重症监护室名称1 as P6911,\n" +
                "                进入时间1 as P6912,\n" +
                "                退出时间1 as P6913,\n" +
                "                重症监护室名称2 as P6914,\n" +
                "                进入时间2 as P6915,\n" +
                "                退出时间2 as P6916,\n" +
                "                重症监护室名称3 as P6917,\n" +
                "                进入时间3 as P6918,\n" +
                "                退出时间3 as P6919,\n" +
                "                重症监护室名称4 as P6920,\n" +
                "                进入时间4 as P6921,\n" +
                "                退出时间4 as P6922,\n" +
                "                重症监护室名称5 as P6923,\n" +
                "                进入时间5 as P6924,\n" +
                "                退出时间5 as P6925,\n" +
                "                医疗费用支付方式代码 as P1,\n" +
                "                总费用 as P7508,\n" +
                "                挂号费 as P7509,\n" +
                "                药品费 as P7510,\n" +
                "                检查费 as P7511,\n" +
                "                自付费用 as P7512,\n" +
                "                是否死亡 as P8508,\n" +
                "                死亡时间 as P8509\n" +
                "  from (select '45038862-0' as 医疗机构代码,\n" +
                "               '新晃侗族自治县人民医院' as 机构名称,\n" +
                "               null as 医疗保险手册,\n" +
                "               null as 健康卡号,\n" +
                "               '01' as 就诊类型,\n" +
                "               a.门诊号 as 就诊卡号,\n" +
                "               a.姓名 as 姓名,\n" +
                "               nvl((select decode(编码, '3', '0', 编码)\n" +
                "                     from 性别\n" +
                "                    where 名称 = a.性别),\n" +
                "                   '0') as 性别,\n" +
                "               to_char(a.出生日期, 'yyyy-mm-dd') as 出生日期,\n" +
                "               null as 年龄,\n" +
                "               '01' as 注册证件类型代码,\n" +
                "               nvl(a.身份证号, '-') as 注册证件号码,\n" +
                "               null as 就诊科室代码,\n" +
                "               1 as 就诊次数,\n" +
                "               to_char(b.记录日期, 'yyyy-mm-dd hh24:mi:ss') as 就诊日期,\n" +
                "               '-' as 主诉,\n" +
                "               null as 主要疾病诊断代码,\n" +
                "               b.诊断描述 as 主要疾病诊断描述,\n" +
                "               null as 其他疾病诊断代码1,\n" +
                "               null as 其他疾病诊断描述1,\n" +
                "               null as 其他疾病诊断代码2,\n" +
                "               null as 其他疾病诊断描述2,\n" +
                "               null as 其他疾病诊断代码3,\n" +
                "               null as 其他疾病诊断描述3,\n" +
                "               null as 其他疾病诊断代码4,\n" +
                "               null as 其他疾病诊断描述4,\n" +
                "               null as 其他疾病诊断代码5,\n" +
                "               null as 其他疾病诊断描述5,\n" +
                "               null as 其他疾病诊断代码6,\n" +
                "               null as 其他疾病诊断描述6,\n" +
                "               null as 其他疾病诊断代码7,\n" +
                "               null as 其他疾病诊断描述7,\n" +
                "               null as 其他疾病诊断代码8,\n" +
                "               null as 其他疾病诊断描述8,\n" +
                "               null as 其他疾病诊断代码9,\n" +
                "               null as 其他疾病诊断描述9,\n" +
                "               null as 其他疾病诊断代码10,\n" +
                "               null as 其他疾病诊断描述10,\n" +
                "               null as 重症监护室名称1,\n" +
                "               null as 进入时间1,\n" +
                "               null as 退出时间1,\n" +
                "               null as 重症监护室名称2,\n" +
                "               null as 进入时间2,\n" +
                "               null as 退出时间2,\n" +
                "               null as 重症监护室名称3,\n" +
                "               null as 进入时间3,\n" +
                "               null as 退出时间3,\n" +
                "               null as 重症监护室名称4,\n" +
                "               null as 进入时间4,\n" +
                "               null as 退出时间4,\n" +
                "               null as 重症监护室名称5,\n" +
                "               null as 进入时间5,\n" +
                "               null as 退出时间5,\n" +
                "               nvl((select substr(编码,1,1) from 医疗付款方式 where 名称 = a.医疗付款方式),\n" +
                "                   '7') as 医疗费用支付方式代码,\n" +
                "               (select nvl(sum(b1.实收金额), 0)\n" +
                "                  from 病人医嘱记录 a1, 门诊费用记录 b1, 病人挂号记录 c1\n" +
                "                 where a1.id = b1.医嘱序号\n" +
                "                   and a1.挂号单 = c1.no\n" +
                "                   and c1.id = b.主页id\n" +
                "                   and b1.记录状态 <> 0) as 总费用,\n" +
                "               (select nvl(sum(a1.实收金额), 0)\n" +
                "                  from 门诊费用记录 a1, 病人挂号记录 b1\n" +
                "                 where a1.no = b1.no\n" +
                "                   and a1.记录性质 = 4\n" +
                "                   and a1.记录状态 <> 0\n" +
                "                   and b1.id = b.主页id) as 挂号费,\n" +
                "               (select nvl(sum(b1.实收金额), 0)\n" +
                "                  from 病人医嘱记录 a1, 门诊费用记录 b1, 病人挂号记录 c1\n" +
                "                 where a1.id = b1.医嘱序号\n" +
                "                   and a1.挂号单 = c1.no\n" +
                "                   and c1.id = b.主页id\n" +
                "                   and b1.记录状态 <> 0\n" +
                "                   and b1.收费类别 in ('5', '6', '7')) as 药品费,\n" +
                "               (select nvl(sum(b1.实收金额), 0)\n" +
                "                  from 病人医嘱记录 a1, 门诊费用记录 b1, 病人挂号记录 c1\n" +
                "                 where a1.id = b1.医嘱序号\n" +
                "                   and a1.挂号单 = c1.no\n" +
                "                   and c1.id = b.主页id\n" +
                "                   and b1.记录状态 <> 0\n" +
                "                   and b1.收费类别 = 'D') as 检查费,\n" +
                "               (select nvl(sum(b1.实收金额), 0)\n" +
                "                  from 病人医嘱记录 a1, 门诊费用记录 b1, 病人挂号记录 c1\n" +
                "                 where a1.id = b1.医嘱序号\n" +
                "                   and a1.挂号单 = c1.no\n" +
                "                   and c1.id = b.主页id\n" +
                "                   and b1.记录状态 <> 0) as 自付费用,\n" +
                "               2 as 是否死亡,\n" +
                "               null as 死亡时间\n" +
                "          from 病人信息 a, 病人诊断记录 b\n" +
                "         where a.病人id = b.病人id\n" +
                "           and b.主页id > 100\n" +
                "           and (REGEXP_LIKE(substr(b.诊断描述, 2, 3), '^J\\d\\d$') OR b.诊断描述 like '%流%感' OR b.诊断描述 like '甲%流' OR b.诊断描述 like '乙%流')\n" +
                "           and b.记录日期 >= trunc(sysdate-7,'DD')\n" +
                "        union all\n" +
                "        select '45038862-0' as 医疗机构代码,\n" +
                "               '重庆市精神卫生中心' as 机构名称,\n" +
                "               null as 医疗保险手册,\n" +
                "               null as 健康卡号,\n" +
                "               '03' as 就诊类型,\n" +
                "               a.住院号 as 就诊卡号,\n" +
                "               a.姓名 as 姓名,\n" +
                "               nvl((select decode(编码, '3', '0', 编码)\n" +
                "                     from 性别\n" +
                "                    where 名称 = a.性别),\n" +
                "                   '0') as 性别,\n" +
                "               to_char(a.出生日期, 'yyyy-mm-dd') as 出生日期,\n" +
                "               null as 年龄,\n" +
                "               '01' as 注册证件类型代码,\n" +
                "               nvl(a.身份证号, '-') as 注册证件号码,\n" +
                "               null as 就诊科室代码,\n" +
                "               b.主页id as 就诊次数,\n" +
                "               to_char(c.入院日期, 'yyyy-mm-dd hh24:mi:ss') as 就诊日期,\n" +
                "               '-' as 主诉,\n" +
                "               null as 主要疾病诊断代码,\n" +
                "               b.诊断描述 as 主要疾病诊断描述,\n" +
                "               null as 其他疾病诊断代码1,\n" +
                "               null as 其他疾病诊断描述1,\n" +
                "               null as 其他疾病诊断代码2,\n" +
                "               null as 其他疾病诊断描述2,\n" +
                "               null as 其他疾病诊断代码3,\n" +
                "               null as 其他疾病诊断描述3,\n" +
                "               null as 其他疾病诊断代码4,\n" +
                "               null as 其他疾病诊断描述4,\n" +
                "               null as 其他疾病诊断代码5,\n" +
                "               null as 其他疾病诊断描述5,\n" +
                "               null as 其他疾病诊断代码6,\n" +
                "               null as 其他疾病诊断描述6,\n" +
                "               null as 其他疾病诊断代码7,\n" +
                "               null as 其他疾病诊断描述7,\n" +
                "               null as 其他疾病诊断代码8,\n" +
                "               null as 其他疾病诊断描述8,\n" +
                "               null as 其他疾病诊断代码9,\n" +
                "               null as 其他疾病诊断描述9,\n" +
                "               null as 其他疾病诊断代码10,\n" +
                "               null as 其他疾病诊断描述10,\n" +
                "               null as 重症监护室名称1,\n" +
                "               null as 进入时间1,\n" +
                "               null as 退出时间1,\n" +
                "               null as 重症监护室名称2,\n" +
                "               null as 进入时间2,\n" +
                "               null as 退出时间2,\n" +
                "               null as 重症监护室名称3,\n" +
                "               null as 进入时间3,\n" +
                "               null as 退出时间3,\n" +
                "               null as 重症监护室名称4,\n" +
                "               null as 进入时间4,\n" +
                "               null as 退出时间4,\n" +
                "               null as 重症监护室名称5,\n" +
                "               null as 进入时间5,\n" +
                "               null as 退出时间5,\n" +
                "               nvl((select substr(编码,1,1) from 医疗付款方式 where 名称 = a.医疗付款方式),\n" +
                "                   '7') as 医疗费用支付方式代码,\n" +
                "               null as 总费用,\n" +
                "               null as 挂号费,\n" +
                "               null as 药品费,\n" +
                "               null as 检查费,\n" +
                "               null as 自付费用,\n" +
                "               2 as 是否死亡,\n" +
                "               null as 死亡时间\n" +
                "          from 病人信息 a, 病人诊断记录 b, 病案主页 c\n" +
                "         where a.病人id = c.病人id\n" +
                "           and b.病人id = c.病人id\n" +
                "           and b.主页id = c.主页id\n" +
                "           and c.出院日期 is null\n" +
                "           and (REGEXP_LIKE(substr(b.诊断描述, 2, 3), '^J\\d\\d$') OR b.诊断描述 like '%流%感' OR b.诊断描述 like '甲%流' OR b.诊断描述 like '乙%流'))\n";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FLU.class));
    }

    /**
     * 获取出院小结数据
     * @return
     */
    public List<HDA> getHDAInfo() {

        String sql = "select distinct 病案号             as P3,\n" +
                "                姓名               as P4,\n" +
                "                性别               as P5,\n" +
                "                年龄               as P7,\n" +
                "                入院日期           as P22,\n" +
                "                入院科别           as P23,\n" +
                "                转科科别           as P24,\n" +
                "                出院日期           as P25,\n" +
                "                出院科别           as P26,\n" +
                "                实际住院天数       as P27,\n" +
                "                入院诊断           as P8600,\n" +
                "                出院诊断           as P8601,\n" +
                "                入院情况及诊疗经过 as P8602,\n" +
                "                出院情况及治疗结果 as P8603,\n" +
                "                出院医嘱           as P8604\n" +
                "  from (select a.住院号 as 病案号,\n" +
                "               a.姓名 as 姓名,\n" +
                "               nvl((select decode(编码, '3', '0', 编码)\n" +
                "                     from 性别\n" +
                "                    where 名称 = a.性别),\n" +
                "                   '0') as 性别,\n" +
                "               null as 年龄,\n" +
                "               to_char(b.入院日期, 'yyyy-mm-dd hh24:mi:ss') as 入院日期,\n" +
                "               (select 工作性质 from 临床部门 where 部门id = b.入院科室id) as 入院科别,\n" +
                "               null as 转科科别,\n" +
                "               to_char(b.出院日期, 'yyyy-mm-dd hh24:mi:ss') as 出院日期,\n" +
                "               (select 工作性质 from 临床部门 where 部门id = b.出院科室id) as 出院科别,\n" +
                "               b.住院天数 as 实际住院天数,\n" +
                "               nvl((select 诊断描述\n" +
                "                     from 病人诊断记录\n" +
                "                    where 记录来源 = 3\n" +
                "                      and 诊断类型 = 2\n" +
                "                      and 诊断次序 = 1\n" +
                "                      and rownum=1 \n" +
                "                      and 病人id = b.病人id\n" +
                "                      and 主页id = b.主页id),\n" +
                "                   '-') as 入院诊断,\n" +
                "               nvl((select 诊断描述\n" +
                "                     from 病人诊断记录\n" +
                "                    where 记录来源 = 3\n" +
                "                      and 诊断类型 = 3\n" +
                "                      and 诊断次序 = 1\n" +
                "                      and rownum=1\n" +
                "                      and 病人id = b.病人id\n" +
                "                      and 主页id = b.主页id),\n" +
                "                   '00.00') as 出院诊断,\n" +
                "               null as 入院情况及诊疗经过,\n" +
                "               null as 出院情况及治疗结果,\n" +
                "               null as 出院医嘱\n" +
                "          from 病人信息 a, 病案主页 b, 病人诊断记录 c\n" +
                "         where a.病人id = b.病人id\n" +
                "           and b.病人id = c.病人id\n" +
                "           and b.主页id = c.主页id\n" +
                "           and b.出院日期  >=trunc(sysdate-7,'DD')\n" +
                "           and (REGEXP_LIKE(substr(c.诊断描述, 2, 3), '^J\\d\\d$') OR c.诊断描述 like '%流%感' OR c.诊断描述 like '甲%流' OR c.诊断描述 like '乙%流'))\n";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HDA.class));
    }

    /**
     * 获取出院数据
     * @return
     */
    public List<HQMS> getHQMSInfo() {

        String sql = "select distinct 医疗机构代码                   as P900,\n" +
                "                机构名称                       as P6891,\n" +
                "                医疗保险手册                   as P686,\n" +
                "                健康卡号                       as P800,\n" +
                "                医疗付款方式                   as P1,\n" +
                "                住院次数                       as P2,\n" +
                "                病案号                         as P3,\n" +
                "                姓名                           as P4,\n" +
                "                性别                           as P5,\n" +
                "                出生日期                       as P6,\n" +
                "                年龄                           as P7,\n" +
                "                婚姻状况                       as P8,\n" +
                "                职业                           as P9,\n" +
                "                出生省份                       as P101,\n" +
                "                出生地市                       as P102,\n" +
                "                出生地县                       as P103,\n" +
                "                民族                           as P11,\n" +
                "                国籍                           as P12,\n" +
                "                身份证号                       as P13,\n" +
                "                现住址                         as P801,\n" +
                "                住宅电话                       as P802,\n" +
                "                现住址邮政编码                 as P803,\n" +
                "                工作单位及地址                 as P14,\n" +
                "                电话                           as P15,\n" +
                "                工作单位邮政编码               as P16,\n" +
                "                户口地址                       as P17,\n" +
                "                户口所在地邮政编码             as P171,\n" +
                "                联系人姓名                     as P18,\n" +
                "                关系                           as P19,\n" +
                "                联系人地址                     as P20,\n" +
                "                入院途径                       as P804,\n" +
                "                联系人电话                     as P21,\n" +
                "                入院日期                       as P22,\n" +
                "                入院科别                       as P23,\n" +
                "                入院病室                       as P231,\n" +
                "                转科科别                       as P24,\n" +
                "                出院日期                       as P25,\n" +
                "                出院科别                       as P26,\n" +
                "                出院病室                       as P261,\n" +
                "                实际住院天数                   as P27,\n" +
                "                门急诊诊断编码                 as P28,\n" +
                "                门急诊诊断描述                 as P281,\n" +
                "                入院时情况                     as P29,\n" +
                "                入院诊断编码                   as P30,\n" +
                "                入院诊断描述                   as P301,\n" +
                "                入院后确诊日期                 as P31,\n" +
                "                主要诊断编码                   as P321,\n" +
                "                主要诊断疾病描述               as P322,\n" +
                "                主要诊断入院病情               as P805,\n" +
                "                主要诊断出院情况               as P323,\n" +
                "                其他诊断编码1                  as P324,\n" +
                "                其他诊断疾病描述1              as P325,\n" +
                "                其他诊断入院病情1              as P806,\n" +
                "                其他诊断出院情况1              as P326,\n" +
                "                其他诊断编码2                  as P327,\n" +
                "                其他诊断疾病描述2              as P328,\n" +
                "                其他诊断入院病情2              as P807,\n" +
                "                其他诊断出院情况2              as P329,\n" +
                "                其他诊断编码3                  as P3291,\n" +
                "                其他诊断疾病描述3              as P3292,\n" +
                "                其他诊断入院病情3              as P808,\n" +
                "                其他诊断出院情况3              as P3293,\n" +
                "                其他诊断编码4                  as P3294,\n" +
                "                其他诊断疾病描述4              as P3295,\n" +
                "                其他诊断入院病情4              as P809,\n" +
                "                其他诊断出院情况4              as P3296,\n" +
                "                其他诊断编码5                  as P3297,\n" +
                "                其他诊断疾病描述5              as P3298,\n" +
                "                其他诊断入院病情5              as P810,\n" +
                "                其他诊断出院情况5              as P3299,\n" +
                "                其他诊断编码6                  as P3281,\n" +
                "                其他诊断疾病描述6              as P3282,\n" +
                "                其他诊断入院病情6              as P811,\n" +
                "                其他诊断出院情况6              as P3283,\n" +
                "                其他诊断编码7                  as P3284,\n" +
                "                其他诊断疾病描述7              as P3285,\n" +
                "                其他诊断入院病情7              as P812,\n" +
                "                其他诊断出院情况7              as P3286,\n" +
                "                其他诊断编码8                  as P3287,\n" +
                "                其他诊断疾病描述8              as P3288,\n" +
                "                其他诊断入院病情8              as P813,\n" +
                "                其他诊断出院情况8              as P3289,\n" +
                "                其他诊断编码9                  as P3271,\n" +
                "                其他诊断疾病描述9              as P3272,\n" +
                "                其他诊断入院病情9              as P814,\n" +
                "                其他诊断出院情况9              as P3273,\n" +
                "                其他诊断编码10                 as P3274,\n" +
                "                其他诊断疾病描述10             as P3275,\n" +
                "                其他诊断入院病情10             as P815,\n" +
                "                其他诊断出院情况10             as P3276,\n" +
                "                医院感染总次数                 as P689,\n" +
                "                病理诊断编码1                  as P351,\n" +
                "                病理诊断名称1                  as P352,\n" +
                "                病理号1                        as P816,\n" +
                "                病理诊断编码2                  as P353,\n" +
                "                病理诊断名称2                  as P354,\n" +
                "                病理号2                        as P817,\n" +
                "                病理诊断编码3                  as P355,\n" +
                "                病理诊断名称3                  as P356,\n" +
                "                病理号3                        as P818,\n" +
                "                损伤中毒的外部因素编码1        as P361,\n" +
                "                损伤中毒的外部因素名称1        as P362,\n" +
                "                损伤中毒的外部因素编码2        as P363,\n" +
                "                损伤中毒的外部因素名称2        as P364,\n" +
                "                损伤中毒的外部因素编码3        as P365,\n" +
                "                损伤中毒的外部因素名称3        as P366,\n" +
                "                过敏源                         as P371,\n" +
                "                过敏药物名称                   as P372,\n" +
                "                HBsAg                          as P38,\n" +
                "                HCVAb                          as P39,\n" +
                "                HIVAb                          as P40,\n" +
                "                门诊与出院诊断符合情况         as P411,\n" +
                "                入院与出院诊断符合情况         as P412,\n" +
                "                术前与术后诊断符合情况         as P413,\n" +
                "                临床与病理诊断符合情况         as P414,\n" +
                "                放射与病理诊断符合情况         as P415,\n" +
                "                抢救次数                       as P421,\n" +
                "                抢救成功次数                   as P422,\n" +
                "                最高诊断依据                   as P687,\n" +
                "                分化程度                       as P688,\n" +
                "                科主任                         as P431,\n" +
                "                主副主任医师                   as P432,\n" +
                "                主治医师                       as P433,\n" +
                "                住院医师                       as P434,\n" +
                "                责任护士                       as P819,\n" +
                "                进修医师                       as P435,\n" +
                "                研究生实习医师                 as P436,\n" +
                "                实习医师                       as P437,\n" +
                "                编码员                         as P438,\n" +
                "                病案质量                       as P44,\n" +
                "                质控医师                       as P45,\n" +
                "                质控护师                       as P46,\n" +
                "                质控日期                       as P47,\n" +
                "                手术操作编码1                  as P490,\n" +
                "                手术操作日期1                  as P491,\n" +
                "                手术级别1                      as P820,\n" +
                "                手术操作名称1                  as P492,\n" +
                "                手术操作部位1                  as P493,\n" +
                "                手术持续时间1                  as P494,\n" +
                "                术者1                          as P495,\n" +
                "                Ⅰ助1                          as P496,\n" +
                "                Ⅱ助1                          as P497,\n" +
                "                麻醉方式1                      as P498,\n" +
                "                麻醉分级1                      as P4981,\n" +
                "                切口愈合等级1                  as P499,\n" +
                "                麻醉医师1                      as P4910,\n" +
                "                手术操作编码2                  as P4911,\n" +
                "                手术操作日期2                  as P4912,\n" +
                "                手术级别2                      as P821,\n" +
                "                手术操作名称2                  as P4913,\n" +
                "                手术操作部位2                  as P4914,\n" +
                "                手术持续时间2                  as P4915,\n" +
                "                术者2                          as P4916,\n" +
                "                Ⅰ助2                          as P4917,\n" +
                "                Ⅱ助2                          as P4918,\n" +
                "                麻醉方式2                      as P4919,\n" +
                "                麻醉分级2                      as P4982,\n" +
                "                切口愈合等级2                  as P4920,\n" +
                "                麻醉医师2                      as P4921,\n" +
                "                手术操作编码3                  as P4922,\n" +
                "                手术操作日期3                  as P4923,\n" +
                "                手术级别3                      as P822,\n" +
                "                手术操作名称3                  as P4924,\n" +
                "                手术操作部位3                  as P4925,\n" +
                "                手术持续时间3                  as P4526,\n" +
                "                术者3                          as P4527,\n" +
                "                Ⅰ助3                          as P4528,\n" +
                "                Ⅱ助3                          as P4529,\n" +
                "                麻醉方式3                      as P4530,\n" +
                "                麻醉分级3                      as P4983,\n" +
                "                切口愈合等级3                  as P4531,\n" +
                "                麻醉医师3                      as P4532,\n" +
                "                手术操作编码4                  as P4533,\n" +
                "                手术操作日期4                  as P4534,\n" +
                "                手术级别4                      as P823,\n" +
                "                手术操作名称4                  as P4535,\n" +
                "                手术操作部位4                  as P4536,\n" +
                "                手术持续时间4                  as P4537,\n" +
                "                术者4                          as P4538,\n" +
                "                Ⅰ助4                          as P4539,\n" +
                "                Ⅱ助4                          as P4540,\n" +
                "                麻醉方式4                      as P4541,\n" +
                "                麻醉分级4                      as P4984,\n" +
                "                切口愈合等级4                  as P4542,\n" +
                "                麻醉医师4                      as P4543,\n" +
                "                手术操作编码5                  as P4544,\n" +
                "                手术操作日期5                  as P4545,\n" +
                "                手术级别5                      as P824,\n" +
                "                手术操作名称5                  as P4546,\n" +
                "                手术操作部位5                  as P4547,\n" +
                "                手术持续时间5                  as P4548,\n" +
                "                术者5                          as P4549,\n" +
                "                Ⅰ助5                          as P4550,\n" +
                "                Ⅱ助5                          as P4551,\n" +
                "                麻醉方式5                      as P4552,\n" +
                "                麻醉分级5                      as P4985,\n" +
                "                切口愈合等级5                  as P4553,\n" +
                "                麻醉医师5                      as P4554,\n" +
                "                手术操作编码6                  as P45002,\n" +
                "                手术操作日期6                  as P45003,\n" +
                "                手术级别6                      as P825,\n" +
                "                手术操作名称6                  as p45004,\n" +
                "                手术操作部位6                  as p45005,\n" +
                "                手术持续时间6                  as p45006,\n" +
                "                术者6                          as p45007,\n" +
                "                Ⅰ助6                          as p45008,\n" +
                "                Ⅱ助6                          as p45009,\n" +
                "                麻醉方式6                      as p45010,\n" +
                "                麻醉分级6                      as p45011,\n" +
                "                切口愈合等级6                  as p45012,\n" +
                "                麻醉医师6                      as p45013,\n" +
                "                手术操作编码7                  as p45014,\n" +
                "                手术操作日期7                  as p45015,\n" +
                "                手术级别7                      as P826,\n" +
                "                手术操作名称7                  as p45016,\n" +
                "                手术操作部位7                  as p45017,\n" +
                "                手术持续时间7                  as p45018,\n" +
                "                术者7                          as p45019,\n" +
                "                Ⅰ助7                          as p45020,\n" +
                "                Ⅱ助7                          as p45021,\n" +
                "                麻醉方式7                      as p45022,\n" +
                "                麻醉分级7                      as p45023,\n" +
                "                切口愈合等级7                  as p45024,\n" +
                "                麻醉医师7                      as p45025,\n" +
                "                手术操作编码8                  as p45026,\n" +
                "                手术操作日期8                  as p45027,\n" +
                "                手术级别8                      as P827,\n" +
                "                手术操作名称8                  as p45028,\n" +
                "                手术操作部位8                  as p45029,\n" +
                "                手术持续时间8                  as p45030,\n" +
                "                术者8                          as p45031,\n" +
                "                Ⅰ助8                          as p45032,\n" +
                "                Ⅱ助8                          as p45033,\n" +
                "                麻醉方式8                      as p45034,\n" +
                "                麻醉分级8                      as p45035,\n" +
                "                切口愈合等级8                  as p45036,\n" +
                "                麻醉医师8                      as p45037,\n" +
                "                手术操作编码9                  as p45038,\n" +
                "                手术操作日期9                  as p45039,\n" +
                "                手术级别9                      as P828,\n" +
                "                手术操作名称9                  as p45040,\n" +
                "                手术操作部位9                  as p45041,\n" +
                "                手术持续时间9                  as p45042,\n" +
                "                术者9                          as p45043,\n" +
                "                Ⅰ助9                          as p45044,\n" +
                "                Ⅱ助9                          as p45045,\n" +
                "                麻醉方式9                      as p45046,\n" +
                "                麻醉分级9                      as p45047,\n" +
                "                切口愈合等级9                  as p45048,\n" +
                "                麻醉医师9                      as p45049,\n" +
                "                手术操作编码10                 as p45050,\n" +
                "                手术操作日期10                 as p45051,\n" +
                "                手术级别10                     as P829,\n" +
                "                手术操作名称10                 as p45052,\n" +
                "                手术操作部位10                 as p45053,\n" +
                "                手术持续时间10                 as p45054,\n" +
                "                术者10                         as p45055,\n" +
                "                Ⅰ助10                         as p45056,\n" +
                "                Ⅱ助10                         as p45057,\n" +
                "                麻醉方式10                     as p45058,\n" +
                "                麻醉分级10                     as p45059,\n" +
                "                切口愈合等级10                 as p45060,\n" +
                "                麻醉医师10                     as p45061,\n" +
                "                特级护理天数                   as P561,\n" +
                "                一级护理天数                   as P562,\n" +
                "                二级护理天数                   as P563,\n" +
                "                三级护理天数                   as P564,\n" +
                "                重症监护室名称1                as P6911,\n" +
                "                进入时间1                      as P6912,\n" +
                "                退出时间1                      as P6913,\n" +
                "                重症监护室名称2                as P6914,\n" +
                "                进入时间2                      as P6915,\n" +
                "                退出时间2                      as P6916,\n" +
                "                重症监护室名称3                as P6917,\n" +
                "                进入时间3                      as P6918,\n" +
                "                退出时间3                      as P6919,\n" +
                "                重症监护室名称4                as P6920,\n" +
                "                进入时间4                      as P6921,\n" +
                "                退出时间4                      as P6922,\n" +
                "                重症监护室名称5                as P6923,\n" +
                "                进入时间5                      as P6924,\n" +
                "                退出时间5                      as P6925,\n" +
                "                死亡患者尸检                   as P57,\n" +
                "                手术治疗检查诊断为本院第一例   as P58,\n" +
                "                手术患者类型                   as P581,\n" +
                "                随诊                           as P60,\n" +
                "                随诊周数                       as P611,\n" +
                "                随诊月数                       as P612,\n" +
                "                随诊年数                       as P613,\n" +
                "                示教病例                       as P59,\n" +
                "                ABO血型                        as P62,\n" +
                "                Rh血型                         as P63,\n" +
                "                输血反应                       as P64,\n" +
                "                红细胞                         as P651,\n" +
                "                血小板                         as P652,\n" +
                "                血浆                           as P653,\n" +
                "                全血                           as P654,\n" +
                "                自体回收                       as P655,\n" +
                "                其它                           as P656,\n" +
                "                年龄不足1周岁的年龄            as P66,\n" +
                "                新生儿出生体重1                as P681,\n" +
                "                新生儿出生体重2                as P682,\n" +
                "                新生儿出生体重3                as P683,\n" +
                "                新生儿出生体重4                as P684,\n" +
                "                新生儿出生体重5                as P685,\n" +
                "                新生儿入院体重                 as P67,\n" +
                "                入院前多少小时                 as P731,\n" +
                "                入院前多少分钟                 as P732,\n" +
                "                入院后多少小时                 as P733,\n" +
                "                入院后多少分钟                 as P734,\n" +
                "                呼吸机使用时间                 as P72,\n" +
                "                是否有出院31天内再住院计划     as P830,\n" +
                "                出院31天再住院计划目的         as P831,\n" +
                "                离院方式                       as P741,\n" +
                "                转入医院名称                   as P742,\n" +
                "                转入社区服务机构乡镇卫生院名称 as P743,\n" +
                "                住院总费用                     as P782,\n" +
                "                住院总费用其中自付金额         as P751,\n" +
                "                一般医疗服务费                 as P752,\n" +
                "                一般治疗操作费                 as P754,\n" +
                "                护理费                         as P755,\n" +
                "                综合医疗服务类其他费用         as P756,\n" +
                "                病理诊断费                     as P757,\n" +
                "                实验室诊断费                   as P758,\n" +
                "                影像学诊断费                   as P759,\n" +
                "                临床诊断项目费                 as P760,\n" +
                "                非手术治疗项目费               as P761,\n" +
                "                临床物理治疗费                 as P762,\n" +
                "                手术治疗费                     as P763,\n" +
                "                麻醉费                         as P764,\n" +
                "                手术费                         as P765,\n" +
                "                康复费                         as P767,\n" +
                "                中医治疗费                     as P768,\n" +
                "                西药费                         as P769,\n" +
                "                抗菌药物费用                   as P770,\n" +
                "                中成药费                       as P771,\n" +
                "                中草药费                       as P772,\n" +
                "                血费                           as P773,\n" +
                "                白蛋白类制品费                 as P774,\n" +
                "                球蛋白类制品费                 as P775,\n" +
                "                凝血因子类制品费               as P776,\n" +
                "                细胞因子类制品费               as P777,\n" +
                "                检查用一次性医用材料费         as P778,\n" +
                "                治疗用一次性医用材料费         as P779,\n" +
                "                手术用一次性医用材料费         as P780,\n" +
                "                其他费                         as P781\n" +
                "  from (select '4484272059' as 医疗机构代码, --\n" +
                "               '新晃侗族自治县人民医院' as 机构名称, --\n" +
                "               null as 医疗保险手册,\n" +
                "               null as 健康卡号,\n" +
                "               nvl((select substr(编码,1,1) from 医疗付款方式 where 名称 = a.医疗付款方式),\n" +
                "                   '7') as 医疗付款方式,\n" +
                "               b.主页id as 住院次数, --\n" +
                "               b.住院号 as 病案号, --\n" +
                "               a.姓名 as 姓名,\n" +
                "               nvl((select decode(编码, '3', '0', 编码)\n" +
                "                     from 性别\n" +
                "                    where 名称 = a.性别),\n" +
                "                   '0') as 性别, --\n" +
                "               to_char(a.出生日期, 'yyyy-mm-dd') as 出生日期,\n" +
                "               null as 年龄,\n" +
                "               nvl((select 编码 from 婚姻状况 where 名称 = a.婚姻状况), '9') as 婚姻状况, --\n" +
                "               null as 职业,\n" +
                "               null as 出生省份,\n" +
                "               null as 出生地市,\n" +
                "               null as 出生地县,\n" +
                "               null as 民族,\n" +
                "               null as 国籍,\n" +
                "               null as 身份证号,\n" +
                "               null as 现住址,\n" +
                "               null as 住宅电话,\n" +
                "               null as 现住址邮政编码,\n" +
                "               null as 工作单位及地址,\n" +
                "               null as 电话,\n" +
                "               null as 工作单位邮政编码,\n" +
                "               null as 户口地址,\n" +
                "               null as 户口所在地邮政编码,\n" +
                "               null as 联系人姓名,\n" +
                "               null as 关系,\n" +
                "               null as 联系人地址,\n" +
                "               null as 入院途径,\n" +
                "               null as 联系人电话,\n" +
                "               to_char(b.入院日期, 'yyyy-mm-dd hh24:mi:ss') as 入院日期, --\n" +
                "               (select 工作性质 from 临床部门 where 部门id = b.入院科室id) as 入院科别, --\n" +
                "               null as 入院病室,\n" +
                "               null as 转科科别,\n" +
                "               to_char(b.出院日期, 'yyyy-mm-dd hh24:mi:ss') as 出院日期, --\n" +
                "               (select 工作性质 from 临床部门 where 部门id = b.出院科室id) as 出院科别, --\n" +
                "               null as 出院病室,\n" +
                "               b.住院天数 as 实际住院天数, --\n" +
                "               null as 门急诊诊断编码,\n" +
                "               nvl((select 诊断描述\n" +
                "                     from 病人诊断记录\n" +
                "                    where 记录来源 = 3\n" +
                "                      and 诊断类型 = 1\n" +
                "                     and rownum=1\n" +
                "                      and 诊断次序 = 1\n" +
                "                      and 病人id = b.病人id\n" +
                "                      and 主页id = b.主页id),\n" +
                "                   '-') as 门急诊诊断描述, --\n" +
                "               null as 入院时情况,\n" +
                "               null as 入院诊断编码,\n" +
                "               null as 入院诊断描述,\n" +
                "               null as 入院后确诊日期,\n" +
                "               nvl((select b.编码\n" +
                "                     from 病人诊断记录 a, 疾病编码目录 b\n" +
                "                    where a.疾病id = b.id\n" +
                "                      and 记录来源 = 3\n" +
                "                      and 诊断类型 = 3\n" +
                "                      and rownum=1\n" +
                "                      and 诊断次序 = 1\n" +
                "                      and 病人id = b.病人id\n" +
                "                      and 主页id = b.主页id),\n" +
                "                   '00.00') as 主要诊断编码, --\n" +
                "               nvl((select 诊断描述\n" +
                "                     from 病人诊断记录\n" +
                "                    where 记录来源 = 3\n" +
                "                    and rownum=1\n" +
                "                      and 诊断类型 = 3\n" +
                "                      and 诊断次序 = 1\n" +
                "                      and 病人id = b.病人id\n" +
                "                      and 主页id = b.主页id),\n" +
                "                   '-') as 主要诊断疾病描述, --\n" +
                "               null as 主要诊断入院病情,\n" +
                "               null as 主要诊断出院情况,\n" +
                "               null as 其他诊断编码1,\n" +
                "               null as 其他诊断疾病描述1,\n" +
                "               null as 其他诊断入院病情1,\n" +
                "               null as 其他诊断出院情况1,\n" +
                "               null as 其他诊断编码2,\n" +
                "               null as 其他诊断疾病描述2,\n" +
                "               null as 其他诊断入院病情2,\n" +
                "               null as 其他诊断出院情况2,\n" +
                "               null as 其他诊断编码3,\n" +
                "               null as 其他诊断疾病描述3,\n" +
                "               null as 其他诊断入院病情3,\n" +
                "               null as 其他诊断出院情况3,\n" +
                "               null as 其他诊断编码4,\n" +
                "               null as 其他诊断疾病描述4,\n" +
                "               null as 其他诊断入院病情4,\n" +
                "               null as 其他诊断出院情况4,\n" +
                "               null as 其他诊断编码5,\n" +
                "               null as 其他诊断疾病描述5,\n" +
                "               null as 其他诊断入院病情5,\n" +
                "               null as 其他诊断出院情况5,\n" +
                "               null as 其他诊断编码6,\n" +
                "               null as 其他诊断疾病描述6,\n" +
                "               null as 其他诊断入院病情6,\n" +
                "               null as 其他诊断出院情况6,\n" +
                "               null as 其他诊断编码7,\n" +
                "               null as 其他诊断疾病描述7,\n" +
                "               null as 其他诊断入院病情7,\n" +
                "               null as 其他诊断出院情况7,\n" +
                "               null as 其他诊断编码8,\n" +
                "               null as 其他诊断疾病描述8,\n" +
                "               null as 其他诊断入院病情8,\n" +
                "               null as 其他诊断出院情况8,\n" +
                "               null as 其他诊断编码9,\n" +
                "               null as 其他诊断疾病描述9,\n" +
                "               null as 其他诊断入院病情9,\n" +
                "               null as 其他诊断出院情况9,\n" +
                "               null as 其他诊断编码10,\n" +
                "               null as 其他诊断疾病描述10,\n" +
                "               null as 其他诊断入院病情10,\n" +
                "               null as 其他诊断出院情况10,\n" +
                "               null as 医院感染总次数,\n" +
                "               null as 病理诊断编码1,\n" +
                "               null as 病理诊断名称1,\n" +
                "               null as 病理号1,\n" +
                "               null as 病理诊断编码2,\n" +
                "               null as 病理诊断名称2,\n" +
                "               null as 病理号2,\n" +
                "               null as 病理诊断编码3,\n" +
                "               null as 病理诊断名称3,\n" +
                "               null as 病理号3,\n" +
                "               null as 损伤中毒的外部因素编码1,\n" +
                "               null as 损伤中毒的外部因素名称1,\n" +
                "               null as 损伤中毒的外部因素编码2,\n" +
                "               null as 损伤中毒的外部因素名称2,\n" +
                "               null as 损伤中毒的外部因素编码3,\n" +
                "               null as 损伤中毒的外部因素名称3,\n" +
                "               null as 过敏源,\n" +
                "               null as 过敏药物名称,\n" +
                "               null as HBsAg,\n" +
                "               null as HCVAb,\n" +
                "               null as HIVAb,\n" +
                "               null as 门诊与出院诊断符合情况,\n" +
                "               null as 入院与出院诊断符合情况,\n" +
                "               null as 术前与术后诊断符合情况,\n" +
                "               null as 临床与病理诊断符合情况,\n" +
                "               null as 放射与病理诊断符合情况,\n" +
                "               null as 抢救次数,\n" +
                "               null as 抢救成功次数,\n" +
                "               null as 最高诊断依据,\n" +
                "               null as 分化程度,\n" +
                "               null as 科主任,\n" +
                "               null as 主副主任医师,\n" +
                "               null as 主治医师,\n" +
                "               null as 住院医师,\n" +
                "               null as 责任护士,\n" +
                "               null as 进修医师,\n" +
                "               null as 研究生实习医师,\n" +
                "               null as 实习医师,\n" +
                "               null as 编码员,\n" +
                "               null as 病案质量,\n" +
                "               null as 质控医师,\n" +
                "               null as 质控护师,\n" +
                "               null as 质控日期,\n" +
                "               (select b1.编码\n" +
                "                  from 病人手麻记录 a1, 疾病编码目录 b1\n" +
                "                 where a1.手术操作id = b1.id\n" +
                "                   and a1.病人id = b.病人id\n" +
                "                   and a1.主页id = b.主页id\n" +
                "                   and rownum = 1) as 手术操作编码1,\n" +
                "               (select to_char(手术日期, 'yyyy-mm-dd hh24:mi:ss')\n" +
                "                  from 病人手麻记录 a1, 疾病编码目录 b1\n" +
                "                 where a1.手术操作id = b1.id\n" +
                "                   and a1.病人id = b.病人id\n" +
                "                   and a1.主页id = b.主页id\n" +
                "                   and rownum = 1) as 手术操作日期1,\n" +
                "               null as 手术级别1,\n" +
                "               (select 名称\n" +
                "                  from 病人手麻记录 a1, 疾病编码目录 b1\n" +
                "                 where a1.手术操作id = b1.id\n" +
                "                   and a1.病人id = b.病人id\n" +
                "                   and a1.主页id = b.主页id\n" +
                "                   and rownum = 1) as 手术操作名称1,\n" +
                "               null as 手术操作部位1,\n" +
                "               null as 手术持续时间1,\n" +
                "               null as 术者1,\n" +
                "               null as Ⅰ助1,\n" +
                "               null as Ⅱ助1,\n" +
                "               null as 麻醉方式1,\n" +
                "               null as 麻醉分级1,\n" +
                "               null as 切口愈合等级1,\n" +
                "               null as 麻醉医师1,\n" +
                "               null as 手术操作编码2,\n" +
                "               null as 手术操作日期2,\n" +
                "               null as 手术级别2,\n" +
                "               null as 手术操作名称2,\n" +
                "               null as 手术操作部位2,\n" +
                "               null as 手术持续时间2,\n" +
                "               null as 术者2,\n" +
                "               null as Ⅰ助2,\n" +
                "               null as Ⅱ助2,\n" +
                "               null as 麻醉方式2,\n" +
                "               null as 麻醉分级2,\n" +
                "               null as 切口愈合等级2,\n" +
                "               null as 麻醉医师2,\n" +
                "               null as 手术操作编码3,\n" +
                "               null as 手术操作日期3,\n" +
                "               null as 手术级别3,\n" +
                "               null as 手术操作名称3,\n" +
                "               null as 手术操作部位3,\n" +
                "               null as 手术持续时间3,\n" +
                "               null as 术者3,\n" +
                "               null as Ⅰ助3,\n" +
                "               null as Ⅱ助3,\n" +
                "               null as 麻醉方式3,\n" +
                "               null as 麻醉分级3,\n" +
                "               null as 切口愈合等级3,\n" +
                "               null as 麻醉医师3,\n" +
                "               null as 手术操作编码4,\n" +
                "               null as 手术操作日期4,\n" +
                "               null as 手术级别4,\n" +
                "               null as 手术操作名称4,\n" +
                "               null as 手术操作部位4,\n" +
                "               null as 手术持续时间4,\n" +
                "               null as 术者4,\n" +
                "               null as Ⅰ助4,\n" +
                "               null as Ⅱ助4,\n" +
                "               null as 麻醉方式4,\n" +
                "               null as 麻醉分级4,\n" +
                "               null as 切口愈合等级4,\n" +
                "               null as 麻醉医师4,\n" +
                "               null as 手术操作编码5,\n" +
                "               null as 手术操作日期5,\n" +
                "               null as 手术级别5,\n" +
                "               null as 手术操作名称5,\n" +
                "               null as 手术操作部位5,\n" +
                "               null as 手术持续时间5,\n" +
                "               null as 术者5,\n" +
                "               null as Ⅰ助5,\n" +
                "               null as Ⅱ助5,\n" +
                "               null as 麻醉方式5,\n" +
                "               null as 麻醉分级5,\n" +
                "               null as 切口愈合等级5,\n" +
                "               null as 麻醉医师5,\n" +
                "               null as 手术操作编码6,\n" +
                "               null as 手术操作日期6,\n" +
                "               null as 手术级别6,\n" +
                "               null as 手术操作名称6,\n" +
                "               null as 手术操作部位6,\n" +
                "               null as 手术持续时间6,\n" +
                "               null as 术者6,\n" +
                "               null as Ⅰ助6,\n" +
                "               null as Ⅱ助6,\n" +
                "               null as 麻醉方式6,\n" +
                "               null as 麻醉分级6,\n" +
                "               null as 切口愈合等级6,\n" +
                "               null as 麻醉医师6,\n" +
                "               null as 手术操作编码7,\n" +
                "               null as 手术操作日期7,\n" +
                "               null as 手术级别7,\n" +
                "               null as 手术操作名称7,\n" +
                "               null as 手术操作部位7,\n" +
                "               null as 手术持续时间7,\n" +
                "               null as 术者7,\n" +
                "               null as Ⅰ助7,\n" +
                "               null as Ⅱ助7,\n" +
                "               null as 麻醉方式7,\n" +
                "               null as 麻醉分级7,\n" +
                "               null as 切口愈合等级7,\n" +
                "               null as 麻醉医师7,\n" +
                "               null as 手术操作编码8,\n" +
                "               null as 手术操作日期8,\n" +
                "               null as 手术级别8,\n" +
                "               null as 手术操作名称8,\n" +
                "               null as 手术操作部位8,\n" +
                "               null as 手术持续时间8,\n" +
                "               null as 术者8,\n" +
                "               null as Ⅰ助8,\n" +
                "               null as Ⅱ助8,\n" +
                "               null as 麻醉方式8,\n" +
                "               null as 麻醉分级8,\n" +
                "               null as 切口愈合等级8,\n" +
                "               null as 麻醉医师8,\n" +
                "               null as 手术操作编码9,\n" +
                "               null as 手术操作日期9,\n" +
                "               null as 手术级别9,\n" +
                "               null as 手术操作名称9,\n" +
                "               null as 手术操作部位9,\n" +
                "               null as 手术持续时间9,\n" +
                "               null as 术者9,\n" +
                "               null as Ⅰ助9,\n" +
                "               null as Ⅱ助9,\n" +
                "               null as 麻醉方式9,\n" +
                "               null as 麻醉分级9,\n" +
                "               null as 切口愈合等级9,\n" +
                "               null as 麻醉医师9,\n" +
                "               null as 手术操作编码10,\n" +
                "               null as 手术操作日期10,\n" +
                "               null as 手术级别10,\n" +
                "               null as 手术操作名称10,\n" +
                "               null as 手术操作部位10,\n" +
                "               null as 手术持续时间10,\n" +
                "               null as 术者10,\n" +
                "               null as Ⅰ助10,\n" +
                "               null as Ⅱ助10,\n" +
                "               null as 麻醉方式10,\n" +
                "               null as 麻醉分级10,\n" +
                "               null as 切口愈合等级10,\n" +
                "               null as 麻醉医师10,\n" +
                "               null as 特级护理天数,\n" +
                "               null as 一级护理天数,\n" +
                "               null as 二级护理天数,\n" +
                "               null as 三级护理天数,\n" +
                "               null as 重症监护室名称1,\n" +
                "               null as 进入时间1,\n" +
                "               null as 退出时间1,\n" +
                "               null as 重症监护室名称2,\n" +
                "               null as 进入时间2,\n" +
                "               null as 退出时间2,\n" +
                "               null as 重症监护室名称3,\n" +
                "               null as 进入时间3,\n" +
                "               null as 退出时间3,\n" +
                "               null as 重症监护室名称4,\n" +
                "               null as 进入时间4,\n" +
                "               null as 退出时间4,\n" +
                "               null as 重症监护室名称5,\n" +
                "               null as 进入时间5,\n" +
                "               null as 退出时间5,\n" +
                "               null as 死亡患者尸检, --\n" +
                "               null as 手术治疗检查诊断为本院第一例,\n" +
                "               null as 手术患者类型,\n" +
                "               null as 随诊,\n" +
                "               null as 随诊周数,\n" +
                "               null as 随诊月数,\n" +
                "               null as 随诊年数,\n" +
                "               null as 示教病例,\n" +
                "               nvl((select decode(编码, '7', '6', '9', '6', 编码)\n" +
                "                     from 血型\n" +
                "                    where 名称 = b.血型),\n" +
                "                   '6') as ABO血型, --\n" +
                "               nvl((select decode(信息名,\n" +
                "                                 '阴',\n" +
                "                                 '1',\n" +
                "                                 '阳',\n" +
                "                                 '2',\n" +
                "                                 '不详',\n" +
                "                                 '3',\n" +
                "                                 '未查',\n" +
                "                                 '4')\n" +
                "                     from 病案主页从表\n" +
                "                    where 病人id = b.病人id\n" +
                "                      and 主页id = b.主页id\n" +
                "                      and 信息值 = 'RH'),\n" +
                "                   '4') as Rh血型, --\n" +
                "               null as 输血反应,\n" +
                "               null as 红细胞,\n" +
                "               null as 血小板,\n" +
                "               null as 血浆,\n" +
                "               null as 全血,\n" +
                "               null as 自体回收,\n" +
                "               null as 其它,\n" +
                "               null as 年龄不足1周岁的年龄,\n" +
                "               null as 新生儿出生体重1,\n" +
                "               null as 新生儿出生体重2,\n" +
                "               null as 新生儿出生体重3,\n" +
                "               null as 新生儿出生体重4,\n" +
                "               null as 新生儿出生体重5,\n" +
                "               null as 新生儿入院体重,\n" +
                "               null as 入院前多少小时,\n" +
                "               null as 入院前多少分钟,\n" +
                "               null as 入院后多少小时,\n" +
                "               null as 入院后多少分钟,\n" +
                "               null as 呼吸机使用时间,\n" +
                "               null as 是否有出院31天内再住院计划,\n" +
                "               null as 出院31天再住院计划目的,\n" +
                "               null as 离院方式,\n" +
                "               '-' as 转入医院名称, --\n" +
                "               '-' as 转入社区服务机构乡镇卫生院名称, --\n" +
                "               (select sum(实收金额)\n" +
                "                  from 住院费用记录\n" +
                "                 where 病人id = b.病人id\n" +
                "                   and 主页id = b.主页id\n" +
                "                   and 记录状态 <> 0) as 住院总费用, --\n" +
                "               null as 住院总费用其中自付金额,\n" +
                "               null as 一般医疗服务费,\n" +
                "               null as 一般治疗操作费,\n" +
                "               null as 护理费,\n" +
                "               null as 综合医疗服务类其他费用,\n" +
                "               null as 病理诊断费,\n" +
                "               null as 实验室诊断费,\n" +
                "               null as 影像学诊断费,\n" +
                "               null as 临床诊断项目费,\n" +
                "               null as 非手术治疗项目费,\n" +
                "               null as 临床物理治疗费,\n" +
                "               null as 手术治疗费,\n" +
                "               null as 麻醉费,\n" +
                "               null as 手术费,\n" +
                "               null as 康复费,\n" +
                "               null as 中医治疗费,\n" +
                "               null as 西药费,\n" +
                "               null as 抗菌药物费用,\n" +
                "               null as 中成药费,\n" +
                "               null as 中草药费,\n" +
                "               null as 血费,\n" +
                "               null as 白蛋白类制品费,\n" +
                "               null as 球蛋白类制品费,\n" +
                "               null as 凝血因子类制品费,\n" +
                "               null as 细胞因子类制品费,\n" +
                "               null as 检查用一次性医用材料费,\n" +
                "               null as 治疗用一次性医用材料费,\n" +
                "               null as 手术用一次性医用材料费,\n" +
                "               null as 其他费\n" +
                "          from 病人信息 a, 病案主页 b, 病人诊断记录 c\n" +
                "         where a.病人id = b.病人id\n" +
                "           and b.病人id = c.病人id\n" +
                "           and b.主页id = c.主页id\n" +
                "           and b.出院日期 >= trunc(sysdate-7,'DD')\n" +
                "           and (REGEXP_LIKE(substr(c.诊断描述, 2, 3), '^J\\d\\d$') OR c.诊断描述 like '%流%感' OR c.诊断描述 like '甲%流' OR c.诊断描述 like '乙%流'))\n";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HQMS.class));
    }

    /**
     * 获取检验数据
     * @return
     */
    public List<LIS> getLISInfo() {

        String sql = "--检验记录，老版lis\n" +
                "--门急诊\n" +
                "Select Decode(a.急诊, 1, '02', '01') As P7501,\n" +
                "       a.门诊号 As P7502,\n" +
                "       To_Char(a.登记时间, 'yyyy-mm-dd hh24:mi:ss') As P7506,\n" +
                "       b.Id As P8000,\n" +
                "       1 As P8001,\n" +
                "       To_Char(nvl(b.检验时间, b.核收时间), 'yyyy-mm-dd hh24:mi:ss') As P8002,\n" +
                "       c.检验结果 As P8003,\n" +
                "       Case\n" +
                "         When c.检验结果 Like '%阳%' Then\n" +
                "          '1'\n" +
                "         Else\n" +
                "          '2'\n" +
                "       End As P8004,\n" +
                "       --参考接口文档RAC041对码文档\n" +
                "       Case\n" +
                "         When sign(instr(d.中文名, '甲')) = 1 Then\n" +
                "          35\n" +
                "         When sign(instr(d.中文名, '乙')) = 1 Then\n" +
                "          36\n" +
                "         Else\n" +
                "          99\n" +
                "       End\n" +
                "       \n" +
                "       As P8005\n" +
                "  From 病人挂号记录 a, 检验标本记录 b, 检验普通结果 c, 诊治所见项目 D\n" +
                " Where a.No = b.挂号单\n" +
                "   And a.记录状态 = 1\n" +
                "   And b.Id = c.检验标本id\n" +
                "   And a.病人id = b.病人id\n" +
                "   And b.病人来源 = 1\n" +
                "      --根据医院甲流和乙流实际检验项目名称，具体为医院甲型流感,乙型流感的对应检验项目的名称\n" +
                "   and d.中文名 like '%流感%'\n" +
                "   And c.检验结果 Like '%阳%'\n" +
                "   And c.检验项目id = d.Id\n" +
                "   and b.审核时间 >= /*B0*/\n" +
                "       trunc(sysdate-7,'DD')\n" +
                "union all\n" +
                "--住院\n" +
                "select '03' as P7501,\n" +
                "       a.住院号 as P7502,\n" +
                "       to_char(a.入院日期, 'yyyy-mm-dd hh24:mi:ss') as P7506,\n" +
                "       b.id as P8000,\n" +
                "       1 as P8001, --胶体金法\n" +
                "       To_Char(nvl(b.检验时间, b.核收时间), 'yyyy-mm-dd hh24:mi:ss') as P8002,\n" +
                "       c.检验结果 as P8003,\n" +
                "       case\n" +
                "         when c.检验结果 like '%阳%' then\n" +
                "          '1'\n" +
                "         else\n" +
                "          '2'\n" +
                "       end as P8004,\n" +
                "       --参考接口文档RAC041对码文档\n" +
                "       decode(sign(instr(d.中文名, '甲')),\n" +
                "              1,\n" +
                "              35,\n" +
                "              decode(sign(instr(d.中文名, '乙')), 1, 36, 99),\n" +
                "              99) as P8005\n" +
                "  from 病案主页 a, 检验标本记录 B, 检验普通结果 C, 诊治所见项目 D\n" +
                " WHERE a.病人id = b.病人id\n" +
                "   and a.主页id = b.主页id\n" +
                "   and b.id = c.检验标本id\n" +
                "   And c.检验项目id = d.Id\n" +
                "   And d.中文名 like '%流感%'\n" +
                "      --根据医院甲流和乙流实际检验项目名称，具体为医院甲型流感,乙型流感的对应检验项目的名称\n" +
                "   and nvl(b.检验时间, b.核收时间) >= /*B0*/\n" +
                "       trunc(sysdate-7,'DD')\n" +
                "\n" +
                " order by P8000\n";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LIS.class));
    }


    public List<QueueInfo> getQueueInfo(String office, String room) {

        String sql = "select * from sys.view_queue_info where status = 0 and office=? and room=? order by sn asc";
//        PreparedStatement preparedStatement = null;
//        List<QueueInfo> queueInfoList= new ArrayList<>();
//        try {
//            preparedStatement = JdbcUtil.getConnection().prepareStatement(
//                    "");
//
//        preparedStatement.setString(1, office);
//        preparedStatement.setString(2, room);
//        ResultSet rs = preparedStatement.executeQuery();
//        while (rs.next()) {
//            QueueInfo queueInfo = new QueueInfo();
//            queueInfo.setPatientName(rs.getString("patientName"));
//            queueInfo.setOffice(rs.getString("office"));
//            queueInfo.setSn(rs.getLong("sn"));
//            queueInfo.setQueueTime(rs.getDate("queueTime"));
//            queueInfo.setRoom(rs.getString("room"));
//            log.info("查询出："+queueInfo.toString());
//            queueInfoList.add(queueInfo);
//        }
//        }catch (SQLException e) {
//            e.printStackTrace();
//        }
        return jdbcTemplate.query(sql, new Object[]{office, room}, new BeanPropertyRowMapper<>(QueueInfo.class));
    }

    public Timestamp getLatestTime(String office, String room) {

        String sql = "select max(callTime) callTime from sys.view_queue_info where office=? and room=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{office, room}, Timestamp.class);
//
//        PreparedStatement preparedStatement = null;
//        Date maxCallTime = null;
//        try {
//            preparedStatement = JdbcUtil.getConnection().prepareStatement(
//                    "select max(callTime) callTime from sys.view_queue_info where office=? and room=?");
//
//            preparedStatement.setString(1, office);
//            preparedStatement.setString(2, room);
//            ResultSet rs = preparedStatement.executeQuery();
//            QueueInfo queueInfo;
//            while (rs.next()) {
//                maxCallTime = rs.getDate("callTime");
//            }
//        }catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return maxCallTime;
    }
}
