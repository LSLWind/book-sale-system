package lsl.java.web.mapper;

import lsl.java.web.entity.Customer;
import lsl.java.web.entity.LoginForm;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CustomerDAO {

    @Select("select * from customer where phone_number=#{phoneNumber} and password=#{password}")
    @Results(id = "customer",value = {
            @Result(column="id",property = "id"),
            @Result(column="name",property = "name"),
            @Result(column="sex",property = "sex"),
            @Result(column="birth_date",property = "birthDate"),
            @Result(column="head_img",property = "headImg"),
            @Result(column="province",property = "province"),
            @Result(column="city",property = "city"),
            @Result(column="address",property = "address"),
            @Result(column="e_mail",property = "email"),
            @Result(column="phone_number",property = "phoneNumber"),
            @Result(column="password",property = "password"),
            @Result(column="changes",property = "changes"),
            @Result(column="points",property = "points")
    })
    Customer getCustomer(String phoneNumber,String password);

    @Update("update customer set name=#{name},sex=#{sex},birth_date=#{birthDate},head_img=#{headImg}," +
            "province=#{province},city=#{city},address=#{address},e_mail=#{email},phone_number=#{phoneNumber}," +
            "password=#{password} where id=#{id}")
    int updateCustomerInfoById(Customer customer);

    @Select("select * from customer where id=#{customerId}")
    @ResultMap("customer")
    Customer getCustomerByCustomerId(long customerId);

    @Insert("insert into customer(phone_number,password,name) values(#{loginName},#{password},'用户')")
    int insertOneCustomerByPhoneNumber(LoginForm loginForm);
}
