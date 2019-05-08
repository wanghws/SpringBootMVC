package com.demo.api.commons.subtable.converter;

import com.demo.api.commons.annotation.SubTable;
import com.demo.api.commons.subtable.Strategy;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.StatementDeParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wanghw on 2019-02-21.
 */
@Slf4j
public abstract class AbstractSQLConverter implements SQLConverter {
    public abstract Statement doConvert(final Statement statement, final Object params);

    // 当前分表策略
    private Strategy currentStrategy;
    // 分表策略缓存
    private static final ConcurrentHashMap<String, Strategy> cache = new ConcurrentHashMap<String, Strategy>();

    /**
     * 是否包含定义的注解
     * @param mapperId Mapper id
     * @return
     */
    private boolean hasAnnotation(String mapperId) {
        if(StringUtils.isBlank(mapperId)) {
            return false;
        }
        //log.debug("[mapperId] " + mapperId);
        // 类名
        String className = StringUtils.substring(mapperId, 0, mapperId.lastIndexOf("."));
        // 方法名
        String methodName = StringUtils.substring(mapperId, mapperId.lastIndexOf(".") + 1);
        //log.debug("[className -> methodName] " + className + " -> " + methodName);
        try {
            Class<?> clazz = Class.forName(className);
            SubTable subTable = clazz.getAnnotation(SubTable.class);
            currentStrategy = cache.get(className);
            if(subTable != null && currentStrategy == null) {
                currentStrategy = subTable.strategy().newInstance();
                cache.put(className, currentStrategy);
            }
            return subTable != null;
        } catch (ClassNotFoundException e) {
            log.error("[hasAnnotation ClassNotFoundException]", e);
        } catch (InstantiationException e) {
            log.error("[hasAnnotation InstantiationException]", e);
        } catch (IllegalAccessException e) {
            log.error("[hasAnnotation IllegalAccessException]", e);
        } catch (SecurityException e) {
            log.error("[hasAnnotation SecurityException]", e);
        }
        return false;
    }

    /**
     * Statement对象反解析成sql语句
     * @param statement
     * @return
     */
    private String doDeParse(Statement statement) {
        if(statement == null) return null;
        StatementDeParser deParser = new StatementDeParser(new StringBuilder());
        statement.accept(deParser);
        return deParser.getBuffer().toString();
    }

    /**
     * 获取分表后的TableName
     * @param baseTableName 基础表
     * @param params 传入参数
     * @return
     */
    protected String getFinalTable(String baseTableName, Object params) {
        Validate.notBlank(baseTableName);
        if(currentStrategy != null) {
            //log.debug("[currentStrategy ClassName] " + currentStrategy.getClass().getName());
            return currentStrategy.getFinalTable(baseTableName, params);
        }
        return baseTableName;
    }

    @Override
    public String convert(Statement statement, Object params, String mapperId) {
        if(!hasAnnotation(mapperId) || statement == null) {
            return null;
        }
        return doDeParse(doConvert(statement, params));
    }
}
