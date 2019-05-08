package com.demo.api.commons.status;

import lombok.Getter;

public enum UserStatus {
    DISABLE(0),//禁用
    NORMAL(1),//正常
    LOCK(2),;//锁定

    @Getter
    private int value;

    UserStatus(int value) {
        this.value = value;
    }

    public static UserStatus getByValue(int value) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        return UserStatus.NORMAL;
    }
}
