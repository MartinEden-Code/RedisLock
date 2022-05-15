package com.congge.entity.file;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Create by tianci
 * 2019/1/10 14:38
 */

@Data
@TableName("upload_file")
@Getter
@Setter
public class UploadFile  implements Serializable {

    @TableField("file_id")
    private String fileId;

    @TableField("file_path")
    private String filePath;

    @TableField("file_size")
    private String fileSize;

    @TableField("file_suffix")
    private String fileSuffix;

    @TableField("file_name")
    private String fileName;

    @TableField("file_md5")
    private String fileMd5;

    @TableField("file_status")
    private Integer fileStatus;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

}
