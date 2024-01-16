package ru.practicum.exploreWithMe.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.LocationDto;
import ru.practicum.exploreWithMe.entity.Location;

@Component
public class LocationMapper {

    public LocationDto convertToLocationDto(Location location) {
        return new LocationDto().setId(location.getId()).setLat(location.getLat()).setLon(location.getLon());
    }
}
