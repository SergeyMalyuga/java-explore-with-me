package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.CommentDto;
import ru.practicum.exploreWithMe.dto.NewCommentDto;

public interface CommentService {

    CommentDto addComment(int userId, NewCommentDto newCommentDto);

    void removeCommentPrivate(int userId, int commentId);

    CommentDto updateCommentPrivate(int userId, int commentId, CommentDto commentDto);
}
