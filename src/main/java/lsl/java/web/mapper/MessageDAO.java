package lsl.java.web.mapper;

import lsl.java.web.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {


    /**
     * 查询与指定用户通信过的商店的id信息列表，因为使用group by 所以只能注入商店的id信息
     */
    @Select("SELECT customer_id,shop_id FROM message WHERE customer_id=#{customerId} GROUP BY shop_id")
    @Results(id = "message",value = {
            @Result(column = "id",property = "id"),
            @Result(column = "customer_id",property = "customerId"),
            @Result(column = "shop_id",property = "shopId"),
            @Result(column = "content",property = "content"),
            @Result(column = "date",property = "date"),
            @Result(column = "state",property = "state"),
            @Result(column = "is_to_shop",property = "isToShop")
    })
    List<Message> getAboutShopMessageByCustomerId(int customerId);

    /**
     *根据指定的用户id与商店id获取一条最新的通信消息
     */
    @Select("SELECT * FROM message WHERE customer_id=#{customerId} AND shop_id=#{shopId} AND date=(\n" +
            "select MAX(date) from message where customer_id=#{customerId} and shop_id=#{shopId})")
    @ResultMap("message")
    Message getNewMessageByCustomerIdAndShopId(int customerId,int shopId);

    @Select("SELECT * FROM message WHERE customer_id=#{customerId} and shop_id=#{shopId} ORDER BY date")
    @ResultMap("message")
    List<Message> getOrderMessageListByCustomerIdAndShopId(int customerId,int shopId);

    @Update("update message set state=0 where customer_id=#{customerId} and shop_id=#{shopId} and state=1")
    int updateMessageState(int customerId,int shopId);


    @Insert("insert into message(customer_id,shop_id,content,date,is_to_shop,state) values" +
            "(#{customerId},#{shopId},#{content},#{date},#{isToShop},#{state})")
    int insertOneMessage(Message message);


    @Select("select customer_id,shop_id from message where shop_id=#{shopId} group by customer_id")
    @ResultMap("message")
    List<Message> getMessageListByShopId(int shopId);
}
