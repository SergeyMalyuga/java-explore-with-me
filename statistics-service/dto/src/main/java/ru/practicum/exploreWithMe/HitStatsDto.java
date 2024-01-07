package ru.practicum.exploreWithMe;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HitStatsDto {

    private String app;
    private String uri;
    private Long hits;

}
