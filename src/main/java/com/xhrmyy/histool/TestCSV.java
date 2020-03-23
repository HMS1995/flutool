package com.xhrmyy.histool;

import com.csvreader.CsvWriter;
import com.xhrmyy.histool.model.FLU;
import com.xhrmyy.histool.util.ReflectUtil;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.Charset;

public class TestCSV {

    public static void main(String[] args) throws IOException {

        CsvWriter csvWriter = new CsvWriter("/1.csv",',', Charset.forName("utf-8"));

        // 写表头
        String[] headers = {"编号","姓名","年龄"};
        String[] content = {"12365","张山","34"};
        csvWriter.writeRecord(headers);
        csvWriter.writeRecord(content);
    }

}
