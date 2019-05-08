package com.demo.api.commons.subtable.converter;

import net.sf.jsqlparser.statement.Statement;

/**
 * Created by wanghw on 2019-02-21.
 */
public interface SQLConverter {
    String convert(Statement statement, Object params, String mapperId);
}
