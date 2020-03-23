package com.xhrmyy.histool.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

public class BaseModel implements Serializable {

    private static final long serialVersionUID = -1716621340634747593L;

    @Override
    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, JSON.DEFFAULT_DATE_FORMAT,
                SerializerFeature.WriteDateUseDateFormat);
    }
}