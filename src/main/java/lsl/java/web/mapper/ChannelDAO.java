package lsl.java.web.mapper;

import lsl.java.web.entity.Channel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface ChannelDAO {

    @Select("select * from channel")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "code",property = "code"),
            @Result(column = "name",property = "name"),
            @Result(column = "book_count",property = "bookCount")
    })
    List<Channel> getChannelList();
}
