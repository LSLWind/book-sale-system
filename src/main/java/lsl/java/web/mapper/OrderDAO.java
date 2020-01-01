package lsl.java.web.mapper;

import lsl.java.web.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDAO {

    @Insert("insert into ord(customer_id,book_id,book_name,book_price,count,bought_date,state,is_finish,shop_id)values" +
            "(#{customerId},#{bookId},#{bookName},#{bookPrice},#{count},#{date},'未发货',0,#{shopId})")
    int insertOrder(Order order);

    @Select("select * from ord where shop_id=#{shopId} and is_finish=0")
    @Results(id = "shopOrder",value = {
            @Result(column = "id",property = "id"),
            @Result(column = "book_name",property = "bookName"),
            @Result(column = "customer_id",property = "customerId"),
            @Result(column = "book_id",property = "bookId"),
            @Result(column = "book_price",property = "bookPrice"),
            @Result(column = "count",property = "count"),
            @Result(column = "bought_date",property = "date"),
            @Result(column = "state",property = "state"),
            @Result(column = "shop_id",property = "shopId"),
            @Result(column = "is_finish",property = "isFinish")
    })
    List<Order> getUnFinishOrderByShopId(int shopId);


    @Select("select * from ord where shop_id=#{shopId} and is_finish=1")
    @ResultMap("shopOrder")
    List<Order> getFinishOrderByShopId(int shopId);


    @Select("select * from ord where customer_id=#{customerId} and is_finish=0")
    @ResultMap("shopOrder")
    List<Order> getUnFinishedOrderListByCustomerId(long customerId);


    @Update("update ord set is_finish=1,state='已完成' where id=#{id}")
    int finishOrder(long id);

    @Update("update ord set state='已发货' where id=#{id}")
    int updateOrderState(long id);
}
