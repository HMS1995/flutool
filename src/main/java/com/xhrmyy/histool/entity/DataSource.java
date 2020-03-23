package com.xhrmyy.histool.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author HuangShiming
 * @Date 2020/3/23
 */
@Component
@ConfigurationProperties(prefix = "datasource")
public class DataSource {

    private String driver_class_name;
}
