package com.congge.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Date currentDate = new Date();
        setFieldValByName("createDate",currentDate,metaObject);
        setFieldValByName("createBy","admin",metaObject);
        setFieldValByName("delFlag",0,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date currentDate = new Date();
        setFieldValByName("updateDate",currentDate,metaObject);
        setFieldValByName("updateBy","admin",metaObject);
        setFieldValByName("delFlag",0,metaObject);
    }
}
