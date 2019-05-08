package com.demo.api.commons.subtable.converter;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;

import java.util.Iterator;

/**
 * Created by wanghw on 2019-02-21.
 */
public class UpdateSQLConverter extends AbstractSQLConverter{
    @Override
    public Statement doConvert(final Statement statement, final Object params) {
        Update update = (Update) statement;
        Iterator<Table> iterator = update.getTables().iterator();
        while(iterator.hasNext()) {
            Table table = iterator.next();
            String baseTableName = table.getName();
            table.setName(super.getFinalTable(baseTableName, params));
        }
        return statement;
    }


}
