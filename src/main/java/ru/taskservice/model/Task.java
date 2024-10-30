package ru.taskservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.taskservice.enums.DefaultStatus;

import java.time.Duration;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Task {
    private UUID id;
    private String name;
    private String description;
    private DefaultStatus defaultStatus;

    private long timeToCompleteSeconds;
    private Duration timeToComplete;

    public Task() {}

    public Task(String name, String description, DefaultStatus defaultStatus, Duration timeToComplete) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.defaultStatus = defaultStatus;
        this.timeToComplete = timeToComplete;
        this.timeToCompleteSeconds = timeToComplete.getSeconds();
    }
}
