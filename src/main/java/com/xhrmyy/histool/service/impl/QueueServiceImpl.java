package com.xhrmyy.histool.service.impl;

import com.xhrmyy.histool.common.BaseResult;
import com.xhrmyy.histool.model.*;
import com.xhrmyy.histool.repository.QueryUtil;
import com.xhrmyy.histool.service.QueueService;
import com.xhrmyy.histool.util.CSVUtil;
import com.xhrmyy.histool.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("queueService")
public class QueueServiceImpl implements QueueService {

    @Autowired
    private QueryUtil queueUtil;
    private static final Logger log = LoggerFactory.getLogger(QueueServiceImpl.class);

    @Override
    public BaseResult toExport() throws Exception {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<FLU> fluInfo = queueUtil.getFLUInfo();
        List<HDA> hdaInfo = queueUtil.getHDAInfo();
        List<HQMS> hqmsInfo = queueUtil.getHQMSInfo();
        List<LIS> lisInfo = queueUtil.getLISInfo();
        List<PDR> pdrInfo = queueUtil.getPDRInfo();
        List<Drug> drugInfo = queueUtil.getDrugInfo();
        List<Examine> examineInfo = queueUtil.getExamineInfo();
        List<Lab> labInfo = queueUtil.getLabInfo();
        List<Fever> feverInfo = queueUtil.getFeverInfo();
        String[] fluHeader = ReflectUtil.getFiledName(fluInfo.get(0));

        // 请先确保导出文件夹已存在
        String fluFilePath = "D://data/flu_" + sdf.format(date) + ".csv";
        //生成csv
        CSVUtil.writeCSV(fluInfo, fluFilePath, fluHeader);
        //压缩
        //ZipUtil.ZipCompress(fluFilePath,"D://data/flu_" + sdf.format(date) + ".zip");
        log.info("flu导出成功");
        String[] hdaHeader = ReflectUtil.getFiledName(new HDA());
        String hdaFilePath = "D://data/hda_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(hdaInfo, hdaFilePath, hdaHeader);
        log.info("hda导出成功");
        String[] hqmsHeader = ReflectUtil.getFiledName(new HQMS());
        String hqmsFilePath = "D://data/hqms_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(hqmsInfo, hqmsFilePath, hqmsHeader);
        log.info("hqms导出成功");
        String[] lisHeader = ReflectUtil.getFiledName(new LIS());
        String lisFilePath = "D://data/lis_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(lisInfo, lisFilePath, lisHeader);
        log.info("lis导出成功");
        String[] pdrHeader = ReflectUtil.getFiledName(new PDR());
        String pdrFilePath = "D://data/pdr_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(pdrInfo, pdrFilePath, pdrHeader);
        log.info("pdr导出成功");
        String[] drugHeader = ReflectUtil.getFiledName(new Drug());
        String drugFilePath = "D://data/drug_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(drugInfo, drugFilePath, drugHeader);
        log.info("drug导出成功");
        String[] feverHeader = ReflectUtil.getFiledName(new Fever());
        String feverFilePath = "D://data/fever_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(feverInfo, feverFilePath, feverHeader);
        log.info("fever导出成功");
        String[] examineHeader = ReflectUtil.getFiledName(new Examine());
        String examineFilePath = "D://data/examine_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(examineInfo, examineFilePath, examineHeader);
        log.info("examine导出成功");
        String[] labHeader = ReflectUtil.getFiledName(new Lab());
        String labFilePath = "D://data/lab_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(labInfo, labFilePath, labHeader);
        log.info("lab导出成功");
        return null;
    }




}
