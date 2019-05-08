package com.demo.api.commons.subtable;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.demo.api.commons.subtable.converter.SQLConverterFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;

/**
 * Created by wanghw on 2019-02-21.
 */
@Slf4j
@Intercepts( { @Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = { Connection.class,Integer.class })})
public class SubTablePlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        String sql = SQLConverterFactory.getGlobalInstance().convert(statementHandler.getBoundSql(), mappedStatement);
        if(StringUtils.isNotBlank(sql)) {
            log.debug("[Modified sql] " + sql);
            Reflections.setFieldValue(statementHandler.getBoundSql(), "sql", sql);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
