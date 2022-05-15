package com.congge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.congge.entity.TUser;
import com.congge.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TUserMapper extends BaseMapper<TUser> {
}
