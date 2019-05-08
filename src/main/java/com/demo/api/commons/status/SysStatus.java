package com.demo.api.commons.status;

import lombok.Getter;

public enum SysStatus {
    NORMAL(1),//正常
    DELETE(2),;//删除

    @Getter
    private int value;

    SysStatus(int value) {
        this.value = value;
    }

    public static SysStatus getByValue(int value) {
        for (SysStatus status : SysStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        return SysStatus.NORMAL;
    }
}
