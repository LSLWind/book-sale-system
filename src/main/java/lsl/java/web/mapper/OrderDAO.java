package lsl.java.web.mapper;

import lsl.java.web.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDAO {

    @Insert("insert into ord(customer_id,book_id,book_name,book_price,count,bought_date)values" +
            "(#{customerId},#{bookId},#{bookName},#{bookPrice},#{count},#{date})")
    int insertOrder(Order order);
}
