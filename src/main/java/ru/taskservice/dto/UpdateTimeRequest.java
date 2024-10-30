package ru.taskservice.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.Duration;

@Getter
@Setter
public class UpdateTimeRequest {
    private Duration duration;
}
