package ru.practicum.exploreWithMe.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.StatisticsClient;
import ru.practicum.exploreWithMe.HitDto;
import ru.practicum.exploreWithMe.HitStatsDto;
import ru.practicum.exploreWithMe.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class StatClient {

    @Autowired
    private StatisticsClient statClient;

    public void setEventView(Event event) {
        List<HitStatsDto> viewsList = statClient.getStats(event.getCreatedOn(), LocalDateTime.now(), true,
                List.of("/events/" + event.getId()));
        if (viewsList.isEmpty()) {
            event.setViews(0);
        } else {
            int hitStatsIndex = 0;
            /* event.setViews(viewsList.get(hitStatsIndex).getHits());*/
        }
    }

    public void addEventView(String ip, String uri) {
        statClient.postHit(new HitDto("ewm-main-service", uri, ip, LocalDateTime.now()));
    }
}
