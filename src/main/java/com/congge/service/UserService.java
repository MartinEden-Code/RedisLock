package com.congge.service;

import com.congge.common.ResponseResult;
import com.congge.entity.User;

public interface UserService {

    ResponseResult getById(String id);
}
