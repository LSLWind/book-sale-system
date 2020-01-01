package lsl.java.web.mapper;

import lsl.java.web.entity.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ShopDAO {

    @Select("select * from shop where phone_number=#{phoneNumber} and password=#{password}")
    @Results(id = "shop",value = {
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "head_img",property = "headImg"),
            @Result(column = "phone_number",property = "phoneNumber"),
            @Result(column = "password",property = "password"),
            @Result(column = "address",property = "address")
    })
    Shop getShopByPhoneNumberAndPassword(String phoneNumber,String password);

    @Select("select id,name,head_img from shop where id=#{shopId}")
    @Results(id = "simpleShop",value = {
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "head_img",property = "headImg")
    })
    Shop getShopNameByShopId(int shopId);
}
