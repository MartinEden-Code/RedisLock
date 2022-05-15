package com.congge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.congge.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 操作数据库dao层
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
