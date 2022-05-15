package com.congge.service;

import com.congge.common.ResponseResult;

public interface OrderService {

    ResponseResult getOrderById(String id);

    ResponseResult createOrder() throws InterruptedException;
}
