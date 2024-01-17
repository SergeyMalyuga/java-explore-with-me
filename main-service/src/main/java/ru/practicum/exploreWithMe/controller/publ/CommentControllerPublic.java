package ru.practicum.exploreWithMe.controller.publ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.CommentDto;
import ru.practicum.exploreWithMe.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentControllerPublic {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public List<CommentDto> getComments(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return commentService.findAllCommentsPublic(from, size);
    }

    @GetMapping("/{evenId}")
    public List<CommentDto> getCommentByEven(@PathVariable(name = "evenId") int evenId,
                                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return commentService.findAllCommentsByEvent(evenId, from, size);
    }
}
