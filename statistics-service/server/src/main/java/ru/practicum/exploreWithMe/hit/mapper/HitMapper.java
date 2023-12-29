package ru.practicum.exploreWithMe.hit.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.HitDto;
import ru.practicum.exploreWithMe.hit.entity.Hit;

@Component
public class HitMapper {

    public Hit convertToHit(HitDto hitDto) {
        return new Hit().setApp(hitDto.getApp())
                .setIp(hitDto.getIp())
                .setUri(hitDto.getUri())
                .setTimestamp(hitDto.getTimestamp());
    }

}
