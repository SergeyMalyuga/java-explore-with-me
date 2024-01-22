package ru.practicum.exploreWithMe.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.CommentDto;
import ru.practicum.exploreWithMe.entity.Comment;

@Component
public class CommentMapper {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EventMapper eventMapper;

    public CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto().setId(comment.getId())
                .setText(comment.getText())
                .setUser(userMapper.convertToUserDto(comment.getUser()))
                .setEvent(eventMapper.convertToEventShortDto(comment.getEvent()))
                .setCreatedOn(comment.getCreatedOn())
                .setStatus(comment.getStatus());

        if (comment.getStatus() != null) {
            commentDto.setPublishedOn(comment.getPublishedOn());
        }
        return commentDto;
    }
}
