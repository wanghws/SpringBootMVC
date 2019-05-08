package com.demo.api.commons.subtable.converter;

import net.sf.jsqlparser.statement.Statement;
/**
 * Created by wanghw on 2019-02-21.
 */
public class NullSqlConverter extends AbstractSQLConverter{
    @Override
    public Statement doConvert(final Statement statement, final Object params) {
        return statement;
    }

}
