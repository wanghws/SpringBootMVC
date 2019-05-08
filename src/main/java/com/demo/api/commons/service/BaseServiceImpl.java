package com.demo.api.commons.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.api.commons.entity.BaseEntity;
import com.demo.api.commons.utils.Sequence;

import javax.annotation.Resource;

/**
 * Created by wanghw on 2019-02-21.
 */
public class BaseServiceImpl<D extends BaseMapper<T>,T extends BaseEntity> extends ServiceImpl<D,T> implements IBaseService<T> {

    @Resource
    private Sequence sequence;

    public boolean save(T entity){
        if(null == entity.getId()){
            entity.setId(sequence.nextId());
        }
        return super.save(entity);
    }

    public Long nextId(){
        return sequence.nextId();
    }
}
