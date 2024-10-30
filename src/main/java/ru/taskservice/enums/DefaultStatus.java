package ru.taskservice.enums;

import lombok.Getter;

@Getter
public enum DefaultStatus {
    todo("Надо сделать"),
    inWork("В работе"),
    done("Сделано");

    private String status;
    DefaultStatus(String status) {
        this.status = status;
    }
    public static DefaultStatus fromString(String status) {
        for (DefaultStatus ds : DefaultStatus.values()) {
            if (ds.getStatus().equalsIgnoreCase(status) || ds.name().equalsIgnoreCase(status)) {
                return ds;
            }
        }
        throw new IllegalArgumentException("Не найдено соответствующее значение для статуса: " + status);
    }

}

