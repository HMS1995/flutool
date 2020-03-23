package com.xhrmyy.histool.controller;

import com.xhrmyy.histool.common.BaseResult;
import com.xhrmyy.histool.service.QueueService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@RestController
@RequestMapping("/flu")
public class QueueController {

    @Autowired
    private QueueService queueService;

    //@RequestMapping("/list")
    //private String getQueueList(@RequestParam String office, @RequestParam String room, @RequestParam String ca) {
    //    //
    //    //Calendar calendar = Calendar.getInstance();
    //    //int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    //    //int month = calendar.get(Calendar.MONTH);
    //    //String parsedString = DigestUtils.md5Hex("WLZX"+(month+1)*(dayOfWeek-1));
    //    //return queueService.getQueueList(office, room).toString();
    //    ////if(ca.equals(parsedString)){
    //    ////    return queueService.getQueueList(office, room).toString();
    //    ////}else{
    //    ////    BaseResult baseResult = new BaseResult();
    //    ////    baseResult.setMessage("服务器异常");
    //    ////    baseResult.setCode(-500);
    //    ////    return baseResult.toString();
    //    ////}
    //}
    //
    //
    //
    //private String toNotify(@RequestParam Long pid, @RequestParam String ca){
    //
    //    Calendar calendar = Calendar.getInstance();
    //    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    //    int month = calendar.get(Calendar.MONTH);
    //    //String parsedString = DigestUtils.md5Hex("WLZX"+(month+1)*(dayOfWeek-1));
    //    //if(ca.equals(parsedString)){
    //        return queueService.toNotify(pid).toString();
    //    //}else{
    //    //    BaseResult baseResult = new BaseResult();
    //    //    baseResult.setMessage("服务器异常");
    //    //    baseResult.setCode(-500);
    //    //    return baseResult.toString();
    //    //}
    //}
}
