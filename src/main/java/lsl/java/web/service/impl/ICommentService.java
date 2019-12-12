package lsl.java.web.service.impl;

import lsl.java.web.entity.Comment;
import lsl.java.web.mapper.CommentDAO;
import lsl.java.web.service.CommentService;
import org.apache.ibatis.annotations.Result;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ICommentService implements CommentService {
    @Resource
    private CommentDAO commentDAO;

    @Override
    public List<Comment> getCommentListByBookId(long bookId){
        return commentDAO.getCommentListByBookId(bookId);
    }
}
