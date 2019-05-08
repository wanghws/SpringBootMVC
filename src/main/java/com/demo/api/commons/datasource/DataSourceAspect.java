package com.demo.api.commons.datasource;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by wanghw on 20180801.
 */
@Slf4j
@Aspect
@Component
public class DataSourceAspect {
    @Resource
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Before("execution(* com.demo.api.service.*.select*(..)) " +
            "|| execution(* com.demo.api.service.*.get*(..))" +
            "|| execution(* com.demo.api.service.*.find*(..))" +
            "|| execution(* com.demo.api.service.*.list*(..))" +
            "|| execution(* com.demo.api.service.*.count*(..))" +
            "|| execution(* com.demo.api.service.*.page*(..))" +
            "|| execution(* com.demo.api.service.*.lambda*(..))" +
            "|| execution(* com.demo.api.service.*.query*(..))")
    public void setReadDataSourceType() {
        Set<String> keySet = dynamicRoutingDataSource.getCurrentDataSources().keySet();
        List<String> slaveList = new ArrayList<>(keySet);
        slaveList.remove("master");
        Collections.shuffle(slaveList);

        dynamicRoutingDataSource.setPrimary(slaveList.get(0));
        log.debug("dataSource切换到："+slaveList.get(0));
    }

    @Before("execution(* com.demo.api.service.*.insert*(..)) " +
            "|| execution(* com.demo.api.service.*.update*(..)) " +
            "|| execution(* com.demo.api.service.*.delete*(..)) " +
            "|| execution(* com.demo.api.service.*.save*(..))")
    public void setWriteDataSourceType() {
        dynamicRoutingDataSource.setPrimary("master");
        log.debug("dataSource切换到：master");
    }
}
