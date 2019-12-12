package lsl.java.web.mapper;

import lsl.java.web.entity.Depository;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DepositoryDAO {

    @Select("select * from depository where book_id=#{bookId} limit 1")
    @Results(id = "depository",value = {
            @Result(column ="id",property = "id"),
            @Result(column = "book_id",property = "bookId"),
            @Result(column = "shop_id",property = "shopId"),
            @Result(column = "shop_name",property = "shopName"),
            @Result(column = "shop_head_img",property = "shopHeadImg"),
            @Result(column = "count",property = "count"),
            @Result(column = "create_date",property = "createDate")
    })
    Depository getDepositoryByBookId(long bookId);


    @Update("update depository set count=#{count} where book_id=#{bookId}")
    int updateDepositoryByBookId(long bookId,int count);


}
