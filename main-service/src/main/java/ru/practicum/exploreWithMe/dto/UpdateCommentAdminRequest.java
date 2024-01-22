package ru.practicum.exploreWithMe.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateCommentAdminRequest {

    String text;
    @NotNull
    CommentStatusAdmin status;
}
