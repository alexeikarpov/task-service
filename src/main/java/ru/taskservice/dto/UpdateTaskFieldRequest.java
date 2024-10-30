package ru.taskservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateTaskFieldRequest {
    private String fieldName;
    private String fieldValue;
}
