package lsl.java.web.service;

import lsl.java.web.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getCommentListByBookId(long bookId);
}
