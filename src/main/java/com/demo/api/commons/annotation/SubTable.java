package com.demo.api.commons.annotation;

import com.demo.api.commons.subtable.Strategy;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by wanghw on 2019-02-21.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface SubTable {

    /**
     * 分表策略class
     * @return
     */
    Class<? extends Strategy> strategy();

}
