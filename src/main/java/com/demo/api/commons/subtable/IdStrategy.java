package com.demo.api.commons.subtable;

import com.demo.api.commons.entity.BaseEntity;

/**
 * Created by wanghw on 2019-02-21.
 */
public class IdStrategy implements Strategy{
    @Override
    public String getFinalTable(String baseTableName, Object params) {
        Long id = -1l;
        if(params instanceof BaseEntity) {
            id = ((BaseEntity)params).getId();
        }else if(params instanceof Long) {
            id = (Long) params;
        }
        return StrategyConfig.getHashTable(baseTableName, "_", id, 10);
    }
}
