package ru.practicum.exploreWithMe.controller.priv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.CommentDto;
import ru.practicum.exploreWithMe.dto.Marker;
import ru.practicum.exploreWithMe.dto.NewCommentDto;
import ru.practicum.exploreWithMe.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@Validated
public class CommentControllerPrivate {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.OnCreate.class)
    public CommentDto postComment(@PathVariable(name = "userId") int userId,
                                  @Valid @RequestBody NewCommentDto newCommentDto) {
        return commentService.addComment(userId, newCommentDto);
    }

    @GetMapping
    public List<CommentDto> getAllComments(@PathVariable(name = "userId") int userId,
                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                           Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return commentService.getAllCommentsPrivate(userId, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentByyId(@PathVariable(name = "commentId") int commentId) {
        return commentService.getById(commentId);
    }

    @PatchMapping("/{commentId}")
    @Validated(Marker.OnUpdate.class)
    public CommentDto patchComment(@PathVariable(name = "userId") int userId,
                                   @PathVariable(name = "commentId") int commentId,
                                   @Valid @RequestBody NewCommentDto updateCommentDto) {
        return commentService.updateCommentPrivate(userId, commentId, updateCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable(name = "userId") int userId,
                           @PathVariable(name = "commentId") int commentId) {
        commentService.removeCommentUser(userId, commentId);
    }
}
