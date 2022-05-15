package com.congge.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.congge.common.Result;
import com.congge.entity.TUser;
import com.congge.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class TUserController {

    @Autowired
    private TUserService userService;

    @GetMapping("/getUserLists")
    public List<TUser> getUserLists(){
        return userService.getUserLists();
    }

    @PostMapping("/user/save")
    public Result saveUser(@RequestBody TUser tUser){
        return userService.saveUser(tUser);
    }

    @GetMapping("/user/delete")
    public Result deleteUser(int id){
        return userService.deleteUser(id);
    }

    @GetMapping("/get/detail")
    public TUser getById(int id){
        return userService.getById(id);
    }

    @GetMapping("/findByPage")
    public IPage findByPage(@RequestParam(value = "currentPage",required = false) int currentPage,@RequestParam(value = "pageNum",required = false)int pageNum){
        return userService.findByPage(currentPage,pageNum);
    }

}
