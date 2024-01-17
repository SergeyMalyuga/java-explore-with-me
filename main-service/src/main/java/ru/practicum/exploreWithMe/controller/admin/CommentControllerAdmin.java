package ru.practicum.exploreWithMe.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.CommentDto;
import ru.practicum.exploreWithMe.dto.UpdateCommentAdminRequest;
import ru.practicum.exploreWithMe.service.CommentService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/comments")
public class CommentControllerAdmin {

    @Autowired
    private CommentService commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable(name = "commentId") int commentId) {
        commentService.removeCommentAdmin(commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable(name = "commentId") int commentId,
                                    @Valid @RequestBody UpdateCommentAdminRequest updateComment) {
        return commentService.updateCommentAdmin(commentId, updateComment);
    }

    @GetMapping
    public List<CommentDto> getComments(@RequestParam(name = "users", required = false) List<Integer> userIds,
                                        @RequestParam(name = "events", required = false) List<Integer> eventIds,
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                        @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                        @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
                                        @RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return commentService.findAllCommentsAdmin(userIds, eventIds, rangeStart, rangeEnd, from, size);
    }
}
