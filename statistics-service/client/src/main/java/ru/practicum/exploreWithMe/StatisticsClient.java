package ru.practicum.exploreWithMe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class StatisticsClient {

    private final RestTemplate rest;


    @Autowired
    public StatisticsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void postHit(HitDto hitDto) {
        rest.exchange("/hit", HttpMethod.POST, new HttpEntity<>(hitDto, defaultHeaders()), Object.class)
                .getStatusCodeValue();
    }

    public List<HitStatsDto> getStats(LocalDateTime start, LocalDateTime end, Boolean isUnique, List<String> uris) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String encodedStartTime = start.format(dtf);
        String encodedEndTime = end.format(dtf);
        Map<String, Object> parameters = Map.of(
                "start", encodedStartTime,
                "end", encodedEndTime,
                "unique", isUnique,
                "uris", uris.toArray()
        );
        return rest.exchange("/stats?start={start}&end={end}&unique={unique}&uris={uris}", HttpMethod.GET,
                new HttpEntity<>(defaultHeaders()), new ParameterizedTypeReference<List<HitStatsDto>>() {
                }, parameters).getBody();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
