# flutool
全国流感医疗服务监测系统CSV文件导出工具
## 使用步骤
1. 修改数据源（本医院使用重庆中联HIS，数据库为oracle，如果使用其他数据库，则自行修改POM文件数据库驱动及class_name）  
在`src/main/resource/application-dev.yml`中,修改为自己HIS数据库的URL，username，password   
2. 修改查询SQL语句  
在`com.xhrmyy.histool.repository.QueryUtil` 中，修改为自己医院的查询SQL，本医院已经自动将查询数据日期修改为大于7天前,即`sysdate-7`  
3. 创建/修改导出数据文件夹  
在`com.xhrmyy.histool.service.impl.QueueServiceImpl` 中，修改导出文件名或目录。注：必须先创建导出文件夹  
4. 修改定时任务执行时间  
在`com.xhrmyy.histool.task.ExportCSVTask` 中, 修改执行任务时间。
`@Scheduled(cron = "0 0 17 * * ?")` 此表达式意为每日17点执行该任务
5. 运行程序  
本程序有两种运行方式，一是打包成war包，丢到tomcat服务器中运行（创建了计划任务），二是执行运行test包中的Test main方法，一次性运行导出（不会创建计划任务）。以后可能会更新一个打包成Jar包的运行方式。  
6. 配合全国流感医疗服务官网提供的前置机程序，实现每天全自动上传。
