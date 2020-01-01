package lsl.java.web.mapper;

import jdk.nashorn.internal.objects.annotations.Setter;
import lsl.java.web.entity.Book;
import org.apache.ibatis.annotations.*;

import javax.annotation.Resource;
import java.util.List;

@Mapper
public interface BookDAO {

    @Select("select * from book where id>=((select max(id) from book)-(select min(id) from book))*RAND()" +
            "+(select min(id) from book) limit 10")
    @Results(id="book",value = {
            @Result(column = "id",property = "id"),
            @Result(column = "channel_id",property = "channelId"),
            @Result(column = "name",property = "name"),
            @Result(column = "author",property = "author"),
            @Result(column = "price",property = "price"),
            @Result(column = "price_discount",property = "discountPrice"),
            @Result(column = "content",property = "content"),
            @Result(column = "category",property = "category"),
            @Result(column = "imgs",property = "imgs"),
            @Result(column = "press_date",property = "pressDate"),
            @Result(column = "print_date",property = "printDate"),
            @Result(column = "press",property = "press"),
            @Result(column = "one_type",property = "oneType"),
            @Result(column = "two_type",property = "twoType"),
            @Result(column = "page_count",property = "pageCount"),
            @Result(column = "words",property = "words"),
            @Result(column = "ISBN",property = "ISBN"),
            @Result(column = "sale_count",property = "saleCount")
    })
    List<Book> getRandomBookList();

    @Select("select * from book where id=#{channelId} order by rand() limit 10")
    @ResultMap("book")
    List<Book> getBookListByChannelId(int channelId);

    @Select("select * from book where id=#{id}")
    @ResultMap("book")
    Book getBookById(long id);

    @Insert("insert into book(channel_id,name,author,price,price_discount,content,category,imgs,press_date,print_date" +
            ",press,one_type,page_count,words,ISBN) values(#{channelId},#{name},#{author},#{price},#{discountPrice},#{content},#{category}," +
            "#{imgs},#{pressDate},#{printDate},#{press},#{oneType},#{pageCount},#{words},#{ISBN})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertOneBook(Book book);


}
