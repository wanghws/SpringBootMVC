package com.demo.api.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.api.commons.entity.BaseEntity;

/**
 * Created by wanghw on 2019-02-21.
 */
public interface IBaseService<T extends BaseEntity> extends IService<T> {
    boolean save(T entity);
    Long nextId();
}
