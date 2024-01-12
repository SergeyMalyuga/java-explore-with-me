package ru.practicum.exploreWithMe.controller.priv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.ResponseRequestDto;
import ru.practicum.exploreWithMe.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
public class RequestControllerPrivate {

    @Autowired
    private RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseRequestDto postRequest(@PathVariable(name = "userId") int userId,
                                          @RequestParam(name = "eventId") int evenId) {
        return requestService.addRequest(userId, evenId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseRequestDto canceledUserRequest(@PathVariable(name = "userId") int userId,
                                                       @PathVariable(name = "requestId") int requestId) {
        return requestService.canceledUserRequest(userId, requestId);
    }

    @GetMapping
    public List<ResponseRequestDto> getUserRequests(@PathVariable(name = "userId") int userId) {
        return requestService.getUserRequests(userId);
    }
}
