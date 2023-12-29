package ru.practicum.exploreWithMe.hit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.HitDto;
import ru.practicum.exploreWithMe.HitStatsDto;
import ru.practicum.exploreWithMe.hit.dao.HitRepository;
import ru.practicum.exploreWithMe.hit.exception.InvalidDataException;
import ru.practicum.exploreWithMe.hit.mapper.HitMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HitServiceImp implements HitService {
    @Autowired
    HitRepository hitRepository;

    @Autowired
    HitMapper hitMapper;

    @Override
    public String postHit(HitDto hitDto) {
        hitRepository.save(hitMapper.convertToHit(hitDto));
        return "Информация сохранена";
    }

    @Override
    public List<HitStatsDto> getStats(LocalDateTime start, LocalDateTime end, Boolean isUnique, String[] urisList) {
        checkValidityDate(start, end);
        if (urisList.length == 0) {
            return getStatsWithoutUri(start, end, isUnique);
        } else {
            return getStatsWithUri(start, end, isUnique, urisList);
        }
    }

    private void checkValidityDate(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new InvalidDataException("Start не может быть после end");
        }
    }

    private List<HitStatsDto> getStatsWithoutUri(LocalDateTime start, LocalDateTime end, Boolean isUnique) {
        if (isUnique) {
            return hitRepository.findAllUniqueStatsWithoutUri(start, end);
        } else {
            return hitRepository.findAllStatsWithoutUri(start, end);
        }
    }

    private List<HitStatsDto> getStatsWithUri(LocalDateTime start, LocalDateTime end, Boolean isUnique,
                                              String[] urisList) {
        if (isUnique) {
            return hitRepository.findAllUniqueStatsWithUri(start, end, urisList);
        } else {
            return hitRepository.findAllStatsWithUri(start, end, urisList);
        }
    }
}

