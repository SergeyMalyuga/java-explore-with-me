package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.CommentDto;
import ru.practicum.exploreWithMe.dto.CommentStatusAdmin;
import ru.practicum.exploreWithMe.dto.NewCommentDto;
import ru.practicum.exploreWithMe.dto.UpdateCommentAdminRequest;
import ru.practicum.exploreWithMe.entity.Comment;
import ru.practicum.exploreWithMe.entity.CommentStatus;
import ru.practicum.exploreWithMe.entity.Event;
import ru.practicum.exploreWithMe.entity.User;
import ru.practicum.exploreWithMe.exception.AccessErrorException;
import ru.practicum.exploreWithMe.exception.CommentAddException;
import ru.practicum.exploreWithMe.exception.NoDataFoundException;
import ru.practicum.exploreWithMe.mapper.CommentMapper;
import ru.practicum.exploreWithMe.repository.CommentRepository;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto addComment(int userId, NewCommentDto newCommentDto) {
        Comment comment = checkValidAddComment(userId, newCommentDto);
        return commentMapper.convertToCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getAllCommentsPrivate(int userId, Integer from, Integer size) {
        User user = checkTheExistenceUser(userId);
        return commentRepository.findAllByUser(user, PageRequest.of((int) Math.ceil((double) from / size),
                size)).stream().map(e -> commentMapper.convertToCommentDto(e)).collect(Collectors.toList());
    }

    @Override
    public CommentDto updateCommentPrivate(int userId, int commentId, NewCommentDto newCommentDto) {
        Comment comment = checkTheExistenceComment(commentId);
        checkTheExistenceUser(userId);
        checkIsUserOwnerByComment(userId, commentId);
        if (newCommentDto.getText() != null) {
            comment.setText(newCommentDto.getText());
            comment.setStatus(CommentStatus.PENDING);
        }
        return commentMapper.convertToCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateCommentAdmin(int commentId, UpdateCommentAdminRequest updateCommentDto) {
        Comment comment = checkValidCommentForUpdateAdmin(commentId);
        updateCommentFieldAdmin(updateCommentDto, comment);
        return commentMapper.convertToCommentDto(commentRepository.save(comment));
    }

    @Override
    public void removeCommentAdmin(int commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> findAllCommentsAdmin(List<Integer> commentIdList, List<Integer> eventIdList,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                                 Integer size) {
        return commentRepository.findCommentsByAdmin(commentIdList, eventIdList, rangeStart, rangeEnd,
                        PageRequest.of((int) Math.ceil((double) from / size), size)).stream()
                .map(e -> commentMapper.convertToCommentDto(e)).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> findAllCommentsPublic(Integer from, Integer size) {
        return commentRepository.findAll(PageRequest.of((int) Math.ceil((double) from / size), size)).stream()
                .map(e -> commentMapper.convertToCommentDto(e)).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> findAllCommentsByEvent(int eventId, Integer from, Integer size) {
        Event event = checkTheExistenceEvent(eventId);
        return commentRepository.findCommentsByEvent(event, PageRequest.of((int) Math.ceil((double) from / size), size))
                .stream().map(e -> commentMapper.convertToCommentDto(e)).collect(Collectors.toList());

    }

    private User checkTheExistenceUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoDataFoundException("User with " + userId
                + " was not found"));
    }

    private Event checkTheExistenceEvent(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NoDataFoundException("Event with " + eventId
                + " was not found"));
    }

    private Comment checkTheExistenceComment(int commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new NoDataFoundException("Comment with "
                + commentId + " was not found"));
    }

    private void checkIsUserOwnerByComment(int userId, int commentId) {
        User user = checkTheExistenceUser(userId);
        Comment comment = checkTheExistenceComment(commentId);
        int initiatorCommentId = comment.getUser().getId();
        if (initiatorCommentId != user.getId()) {
            throw new AccessErrorException("You are not the creator of the comment.");
        }
    }

    private Comment checkValidAddComment(int userId, NewCommentDto newCommentDto) {
        Event event = checkTheExistenceEvent(newCommentDto.getEvent());
        User user = checkTheExistenceUser(userId);
        checkIsUserAddCommentEarly(user.getId(), event.getId());
        return createCommentByRequest(newCommentDto, event, user);
    }

    private void checkIsUserAddCommentEarly(int userId, int eventId) {
        Comment comment = commentRepository.findByUserEvent(userId, eventId);
        if (comment != null) {
            throw new CommentAddException("The user has already left a comment on the event");
        }
    }

    private Comment createCommentByRequest(NewCommentDto newCommentDto, Event event, User user) {
        return new Comment().setUser(user).setEvent(event).setStatus(CommentStatus.PENDING)
                .setCreatedOn(LocalDateTime.now()).setText(newCommentDto.getText());

    }

    private CommentStatus convertToCommentStatus(CommentStatusAdmin commentStatusAdmin) {
        if (commentStatusAdmin.equals(CommentStatusAdmin.CONFIRMED)) {
            return CommentStatus.PUBLISHED;
        }
        return CommentStatus.CANCELED;
    }

    private Comment checkValidCommentForUpdateAdmin(int commentId) {
        Comment comment = checkTheExistenceComment(commentId);
        if (!comment.getStatus().equals(CommentStatus.PENDING)) {
            throw new CommentAddException("You can only change comments with status \"PENDING\"");
        }
        return comment;
    }

    private void updateCommentFieldAdmin(UpdateCommentAdminRequest updateCommentDto, Comment comment) {
        if (updateCommentDto.getText() != null) {
            comment.setText(updateCommentDto.getText());
        }
        if (updateCommentDto.getStatus().equals(CommentStatusAdmin.CONFIRMED)) {
            comment.setPublishedOn(LocalDateTime.now());
        }
        comment.setStatus(convertToCommentStatus(updateCommentDto.getStatus()));
    }
}
