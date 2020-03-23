package com.xhrmyy.histool.task;

import com.xhrmyy.histool.service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ExportCSVTask {

    @Autowired
    QueueService queueService;
    private static final Logger log = LoggerFactory.getLogger(ExportCSVTask.class);
    // 定时任务,每天17点00运行
    @Scheduled(cron = "0 0 17 * * ?")
    public void export() {
        try {
            log.info("开始执行导出csv任务");
            queueService.toExport();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
        }
    }

}
