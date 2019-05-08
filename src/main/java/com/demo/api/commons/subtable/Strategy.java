package com.demo.api.commons.subtable;

/**
 * Created by wanghw on 2019-02-21.
 */
public interface Strategy {
    String getFinalTable(String baseTableName, Object params);
}
