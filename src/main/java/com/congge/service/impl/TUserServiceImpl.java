package com.congge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.congge.common.Result;
import com.congge.common.utils.IdUtil;
import com.congge.entity.TUser;
import com.congge.mapper.TUserMapper;
import com.congge.service.TUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TUserServiceImpl implements TUserService {

    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public List<TUser> getUserLists() {
        QueryWrapper queryWrapper = new QueryWrapper();
        return tUserMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveUser(TUser user) {
        //检查当前用户是否存在，若存在，则修改
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",user.getName());
        TUser tUser = tUserMapper.selectOne(queryWrapper);
        if(tUser != null){
            BeanUtils.copyProperties(user,tUser);
            tUserMapper.updateById(tUser);
            return new Result("修改成功",true);
        }else {
            try {
                user.setId(IdUtil.createId());
                tUserMapper.insert(user);
                return new Result("保存成功",true);
            }catch (Exception e){
                return new Result("保存失败",false);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteUser(int id) {
        tUserMapper.deleteById(id);
        return new Result("删除成功",true);
    }

    @Override
    public TUser getById(int id) {
        return tUserMapper.selectById(id);
    }

    @Override
    public IPage findByPage(int currentPage, int pageNum) {
        if(currentPage <= 0 || pageNum<=0 ){
            currentPage =1;
            pageNum=5;
        }
        Page<TUser> pageInfo = new Page<>(currentPage, pageNum);
        QueryWrapper queryWrapper = new QueryWrapper();
        Page page = tUserMapper.selectPage(pageInfo, queryWrapper);
        int count = tUserMapper.selectCount(queryWrapper);
        pageInfo.setTotal(count);
        pageInfo.setRecords(page.getRecords());
        pageInfo.setCurrent(currentPage);
        pageInfo.setPages(page.getPages());
        return pageInfo;
    }

}
