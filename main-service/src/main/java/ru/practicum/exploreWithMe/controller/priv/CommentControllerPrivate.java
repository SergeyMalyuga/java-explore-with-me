package ru.practicum.exploreWithMe.controller.priv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.CommentDto;
import ru.practicum.exploreWithMe.dto.NewCommentDto;
import ru.practicum.exploreWithMe.service.CommentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/comments")
public class CommentControllerPrivate {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public CommentDto postComment(@PathVariable(name = "userId") int userId,
                                  @Valid @RequestBody NewCommentDto newCommentDto) {
        return commentService.addComment(userId, newCommentDto);
    }
}
