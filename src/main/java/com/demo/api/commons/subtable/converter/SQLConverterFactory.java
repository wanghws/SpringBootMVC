package com.demo.api.commons.subtable.converter;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by wanghw on 2019-02-21.
 */
@Slf4j
public class SQLConverterFactory {
    private static final SQLConverterFactory global = new SQLConverterFactory();
    public static SQLConverterFactory getGlobalInstance() {
        return global;
    }

    private Map<Type, SQLConverter> converterMap = new HashMap<Type, SQLConverter>();

    private SQLConverterFactory() {
        converterMap.put(Insert.class, new InsertSQLConverter());
        converterMap.put(Update.class, new UpdateSQLConverter());
        converterMap.put(Delete.class, new DeleteSQLConverter());
        converterMap.put(Select.class, new SelectSQLConverter());
    }

    public String convert(BoundSql boundSql, MappedStatement mappedStatement) {
        String sql = boundSql.getSql();
        Object params = boundSql.getParameterObject();
        String mapperId = mappedStatement.getId();
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if(statement != null) {
                return getConverter(statement.getClass()).convert(statement, params, mapperId);
            }
        } catch (JSQLParserException e) {
            log.error("[convert -> JSQLParserException]", e);
        }
        return sql;
    }

    public SQLConverter getConverter(Type type) {
        SQLConverter converter = converterMap.get(type);
        if(converter == null){
            converter = new NullSqlConverter();
        }
        return converter;
    }

}
