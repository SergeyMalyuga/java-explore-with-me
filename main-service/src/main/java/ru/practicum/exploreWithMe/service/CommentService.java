package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.CommentDto;
import ru.practicum.exploreWithMe.dto.NewCommentDto;
import ru.practicum.exploreWithMe.dto.UpdateCommentAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    CommentDto addComment(int userId, NewCommentDto newCommentDto);

    void removeCommentAdmin(int commentId);

    void removeCommentUser(int userId, int commentId);

    List<CommentDto> getAllCommentsPrivate(int userId, Integer from, Integer size);

    CommentDto updateCommentPrivate(int userId, int commentId, NewCommentDto updateCommentDto);

    CommentDto updateCommentAdmin(int commentId, UpdateCommentAdminRequest updateCommentDto);

    CommentDto getById(int commentId);

    List<CommentDto> getAllCommentsAdmin(List<Integer> commentIdList, List<Integer> eventId, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Integer from, Integer size);

    List<CommentDto> getAllCommentsPublic(Integer from, Integer size);

    List<CommentDto> getAllCommentsByEvent(int eventId, Integer from, Integer size);

}
