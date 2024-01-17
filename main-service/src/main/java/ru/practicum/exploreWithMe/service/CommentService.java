package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.CommentDto;
import ru.practicum.exploreWithMe.dto.NewCommentDto;
import ru.practicum.exploreWithMe.dto.UpdateCommentAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    CommentDto addComment(int userId, NewCommentDto newCommentDto);

    void removeCommentAdmin(int commentId);

    List<CommentDto> getAllCommentsPrivate(int userId, Integer from, Integer size);

    CommentDto updateCommentPrivate(int userId, int commentId, NewCommentDto updateCommentDto);

    CommentDto updateCommentAdmin(int commentId, UpdateCommentAdminRequest updateCommentDto);

    List<CommentDto> findAllCommentsAdmin(List<Integer> commentIdList, List<Integer> eventId, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Integer from, Integer size);

    List<CommentDto> findAllCommentsPublic(Integer from, Integer size);

    List<CommentDto> findAllCommentsByEvent(int eventId, Integer from, Integer size);

}
