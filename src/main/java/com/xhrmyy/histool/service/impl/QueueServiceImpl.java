package com.xhrmyy.histool.service.impl;

import com.csvreader.CsvWriter;
import com.xhrmyy.histool.common.BaseResult;
import com.xhrmyy.histool.entity.QueueInfo;
import com.xhrmyy.histool.model.*;
import com.xhrmyy.histool.repository.QueryUtil;
import com.xhrmyy.histool.service.QueueService;
import com.xhrmyy.histool.util.CSVUtil;
import com.xhrmyy.histool.util.ReflectUtil;
import com.xhrmyy.histool.util.ZipUtil;
import org.apache.commons.net.ntp.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.xhrmyy.histool.util.CSVUtil.writeCSV;

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
        String fluFilePath = "D://data/flu_" + sdf.format(date) + ".csv";
        //生成csv
        CSVUtil.writeCSV(fluInfo, fluFilePath, fluHeader);
        //压缩
        ZipUtil.ZipCompress(fluFilePath,"D://data/flu_" + sdf.format(date) + ".zip");

        String[] hdaHeader = ReflectUtil.getFiledName(hdaInfo.get(0));
        String hdaFilePath = "D://data/hda_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(hdaInfo, hdaFilePath, hdaHeader);
        //压缩
        ZipUtil.ZipCompress(fluFilePath,"D://data/hda_" + sdf.format(date) + ".zip");

        String[] hqmsHeader = ReflectUtil.getFiledName(hqmsInfo.get(0));
        String hqmsFilePath = "D://data/hqms_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(hqmsInfo, hqmsFilePath, hqmsHeader);
        //压缩
        ZipUtil.ZipCompress(fluFilePath,"D://data/hqms_" + sdf.format(date) + ".zip");

        String[] lisHeader = ReflectUtil.getFiledName(lisInfo.get(0));
        String lisFilePath = "D://data/lis_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(lisInfo, lisFilePath, lisHeader);
        //压缩
        ZipUtil.ZipCompress(fluFilePath,"D://data/lis_" + sdf.format(date) + ".zip");

        String[] pdrHeader = ReflectUtil.getFiledName(pdrInfo.get(0));
        String pdrFilePath = "D://data/pdr_" + sdf.format(date) + ".csv";
        CSVUtil.writeCSV(pdrInfo, pdrFilePath, pdrHeader);
        //压缩
        ZipUtil.ZipCompress(fluFilePath,"D://data/pdr_" + sdf.format(date) + ".zip");

        return null;
    }




}
