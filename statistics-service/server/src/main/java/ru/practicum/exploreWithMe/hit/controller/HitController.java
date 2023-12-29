package ru.practicum.exploreWithMe.hit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.HitDto;
import ru.practicum.exploreWithMe.HitStatsDto;
import ru.practicum.exploreWithMe.hit.service.HitService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class HitController {

    @Autowired
    private HitService hitService;

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String postHit(@Valid @RequestBody HitDto hitDto) {
        return hitService.postHit(hitDto);
    }

    @GetMapping("/stats")
    public List<HitStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                      @RequestParam(name = "unique", defaultValue = "false") Boolean isUnique,
                                      @RequestParam(name = "uris", defaultValue = "") String[] urisList) {
        return hitService.getStats(start, end, isUnique, urisList);
    }
}
