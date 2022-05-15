package com.congge.entity.file;

import lombok.Data;

/**
 * 断点上传和分片上传文件相关的参数类
 */
@Data
public class FileForm {

    private String md5;

    private String uuid;

    private String date;

    private String name;

    private String size;

    private String total;

    private String index;

    private String action;

    private String partMd5;
}
