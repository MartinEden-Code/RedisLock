package com.congge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.congge.common.Result;
import com.congge.entity.TUser;

import java.util.List;

public interface TUserService {

    List<TUser> getUserLists();

    Result saveUser(TUser tUser);

    Result deleteUser(int id);

    TUser getById(int id);

    IPage findByPage(int currentPage, int pageNum);
}
