package com.demo.api.commons.subtable;

/**
 * Created by wanghw on 2019-02-21.
 */
public class StrategyConfig {
    /**
     * 获取散列分表名
     * @param table 	基本表名
     * @param separator 分隔符（基础表和散列中间的分隔符）
     * @param flag 		散列依据（分表字段的值，一般为主键）
     * @param n			散列大小（分表的数量）
     * @return
     */
    public static String getHashTable(String table, String separator, long flag, int n) {
        if (flag < 0 || n <= 0) {
            return table;
        }
        // 分表后缀
        long suffix = flag % n;
        // 后缀长度
        int suffixLength = Long.toString(suffix).length();
        // 散列长度
        int nLength = Integer.toString(n).length() - 1;
        // 基础表
        StringBuilder tableBuilder = new StringBuilder(table).append(separator);
        // 循环添加前缀：0
        for (int i = 0; i < (nLength - suffixLength); ++i)
            tableBuilder.append(0);
        // 添加后缀
        tableBuilder.append(suffix);
        return tableBuilder.toString();
    }
}
