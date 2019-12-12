package lsl.java.web.mapper;

import lsl.java.web.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentDAO {

    @Select("select * from comment where book_id=#{bookId}")
    @Results(id="comment",value = {
            @Result(column = "id",property = "id"),
            @Result(column = "book_id",property = "bookId"),
            @Result(column = "customer_id",property = "customerId"),
            @Result(column = "customer_name",property = "customerName"),
            @Result(column = "customer_head_img",property = "customerHeadImg"),
            @Result(column = "comment_id",property = "commentId"),
            @Result(column = "content",property = "content")
    })
    List<Comment> getCommentListByBookId(long bookId);

}
