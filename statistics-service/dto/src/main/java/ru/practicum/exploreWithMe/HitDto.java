package ru.practicum.exploreWithMe;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class HitDto {

    private Long id;
    @NotBlank(message = "Значение app не может быть пустым.")
    private String app;
    @NotBlank(message = "Значение URL не может быть пустым.")
    private String uri;
    @NotBlank(message = "Значение IP не может быть пустым.")
    private String ip;
    @NotNull(message = "Значение timestamp не может быть пустым.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public HitDto(String app, String uri, String ip, LocalDateTime timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    public HitDto(String app, String uri, String ip) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;

    }

    public HitDto(String app) {
        this.app = app;

    }
}
