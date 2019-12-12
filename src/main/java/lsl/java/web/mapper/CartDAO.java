package lsl.java.web.mapper;

import lsl.java.web.entity.Cart;
import lsl.java.web.entity.Customer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartDAO {
    //查找指定用户的指定图书购物车是否存在
    @Select("select * from cart where customer_id=#{customerId} and book_id=#{bookId}")
    @Results(id = "cart",value = {
            @Result(column = "id",property = "id"),
            @Result(column = "book_id",property = "bookId"),
            @Result(column = "customer_id",property = "customerId"),
            @Result(column = "book_name",property = "bookName"),
            @Result(column = "book_price",property = "bookPrice"),
            @Result(column = "count",property = "count"),
            @Result(column = "add_date",property = "addDate"),
            @Result(column = "book_img",property = "bookImg")
    })
    Cart getCartByCustomerIdAndBookId(int customerId,long bookId);

    //插入一条购物车数据记录
    @Insert("insert into cart(book_id,customer_id,book_name,book_price,count,add_date,book_img) values(#{bookId},#{customerId},#{bookName},#{bookPrice},#{count},#{addDate},#{bookImg})")
    int insertCart(Cart cart);

    //查找购物车所有记录
    @Select("select * from cart where customer_id=#{customerId}")
    @ResultMap("cart")
    List<Cart> getCartByCustomerId(int customerId);

    @Delete("delete from cart where id=#{cartId}")
    int deleteCartByCartId(int cartId);

}
