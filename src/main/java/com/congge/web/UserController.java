package com.congge.web;

import com.congge.common.ResponseResult;
import com.congge.entity.User;
import com.congge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get/{id}")
    public ResponseResult getById(@PathVariable("id")String id){
        return userService.getById(id);
    }

}
