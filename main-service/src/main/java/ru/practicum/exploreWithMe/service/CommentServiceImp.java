package ru.practicum.exploreWithMe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.CommentDto;
import ru.practicum.exploreWithMe.dto.NewCommentDto;
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

@Service
public class CommentServiceImp implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public CommentDto addComment(int userId, NewCommentDto newCommentDto) {
        Comment comment = checkValidAddComment(userId, newCommentDto);
        return commentMapper.convertToCommentDto(commentRepository.save(comment));
    }

    @Override
    public void removeCommentPrivate(int userId, int commentId) {
        checkTheExistenceComment(commentId);
        checkTheExistenceUser(userId);
        checkIsUserOwnerByComment(userId, commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto updateCommentPrivate(int userId, int commentId, CommentDto newCommentDto) {
        Comment comment = checkTheExistenceComment(commentId);
        checkTheExistenceUser(userId);
        checkIsUserOwnerByComment(userId, commentId);
        if (newCommentDto.getText() != null) {
            comment.setText(newCommentDto.getText());
        }
        if (newCommentDto.getStatus() != null) {
            comment.setStatus(newCommentDto.getStatus());
        }
        return commentMapper.convertToCommentDto(commentRepository.save(comment));
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
}
