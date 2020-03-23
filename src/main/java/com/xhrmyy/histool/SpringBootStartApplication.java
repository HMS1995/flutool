package com.xhrmyy.histool;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @Description:
 * @Author HuangShiming
 * @Date 2018/12/5 0005
 */
public class SpringBootStartApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

        // 注意这里要指向原先用main方法执行的Application启动类 
        return builder.sources(HisToolApplication.class);
    }
}

