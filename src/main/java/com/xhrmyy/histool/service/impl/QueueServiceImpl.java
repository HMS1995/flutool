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

        String[] fluHeader = ReflectUtil.getFiledName(fluInfo.get(0));
        // 请先确保导出文件夹已存在
        String fluFilePath = "D://data/flu_" + sdf.format(date) + ".csv";
        //生成csv
        CSVUtil.writeCSV(fluInfo, fluFilePath, fluHeader);
        //压缩
        //ZipUtil.ZipCompress(fluFilePath,"D://data/flu_" + sdf.format(date) + ".zip");
        log.info("flu导出成功");
        String[] hdaHeader = ReflectUtil.getFiledName(hdaInfo.get(0));
        String hdaFilePath = "D://data/hda_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(hdaInfo, hdaFilePath, hdaHeader);
        //压缩
        //ZipUtil.ZipCompress(hdaFilePath, "D://data/hda_" + sdf.format(date) + ".zip");
        log.info("hda导出成功");
        String[] hqmsHeader = ReflectUtil.getFiledName(hqmsInfo.get(0));
        String hqmsFilePath = "D://data/hqms_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(hqmsInfo, hqmsFilePath, hqmsHeader);
        //压缩
        //ZipUtil.ZipCompress(hqmsFilePath, "D://data/hqms_" + sdf.format(date) + ".zip");
        log.info("hqms导出成功");
        String[] lisHeader = ReflectUtil.getFiledName(lisInfo.get(0));
        String lisFilePath = "D://data/lis_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(lisInfo, lisFilePath, lisHeader);
        //压缩
        //ZipUtil.ZipCompress(lisFilePath, "D://data/lis_" + sdf.format(date) + ".zip");
        log.info("lis导出成功");
        String[] pdrHeader = ReflectUtil.getFiledName(pdrInfo.get(0));
        String pdrFilePath = "D://data/pdr_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(pdrInfo, pdrFilePath, pdrHeader);
        //压缩
        //ZipUtil.ZipCompress(pdrFilePath, "D://data/pdr_" + sdf.format(date) + ".zip");
        log.info("pdr导出成功");
        return null;
    }




}
