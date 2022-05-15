package com.congge.service.impl;

import com.congge.common.ResponseResult;
import com.congge.common.enums.ExceptionConst;
import com.congge.common.exception.BusinessException;
import com.congge.entity.User;
import com.congge.mapper.UserMapper;
import com.congge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseResult getById(String id) {
        User user = userMapper.selectById(id);
        if(user==null){
            throw new BusinessException(ExceptionConst.DATA_NOT_EXISTS);
        }
        return ResponseResult.success(user,200);
    }

}
