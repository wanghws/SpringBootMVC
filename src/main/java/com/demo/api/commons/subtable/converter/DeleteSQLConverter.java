package com.demo.api.commons.subtable.converter;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;

/**
 * Created by wanghw on 2019-02-21.
 */
public class DeleteSQLConverter extends AbstractSQLConverter {
    @Override
    public Statement doConvert(final Statement statement, final Object params) {
        Delete delete = (Delete) statement;
        Table table = delete.getTable();
        String baseTableName = table.getName();
        table.setName(super.getFinalTable(baseTableName, params));
        return statement;
    }

}
