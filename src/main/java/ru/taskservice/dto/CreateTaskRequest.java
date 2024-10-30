package ru.taskservice.dto;

import lombok.Getter;
import lombok.Setter;
import ru.taskservice.enums.DefaultStatus;

import java.time.Duration;

@Getter
@Setter
public class CreateTaskRequest {
    private String name;
    private String description;
    private DefaultStatus defaultStatus;
    private Duration timeToComplete;

}

