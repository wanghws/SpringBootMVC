package com.demo.api.commons.subtable.converter;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

/**
 * Created by wanghw on 2019-02-21.
 */
public class InsertSQLConverter extends AbstractSQLConverter{
    @Override
    public Statement doConvert(final Statement statement, final Object params) {
        Insert insert = (Insert) statement;
        Table table = insert.getTable();
        String baseTableName = table.getName();
        table.setName(super.getFinalTable(baseTableName, params));
        return statement;
    }
}
