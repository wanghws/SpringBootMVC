package com.demo.api.commons.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by wanghw on 2019-03-11.
 */
public class SysExcetpion extends Exception {
    public SysExcetpion(String code){
        super(code);
        this.code = code;
    }
    @Getter
    @Setter
    private String code;
}
