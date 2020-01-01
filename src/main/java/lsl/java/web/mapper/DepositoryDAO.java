package lsl.java.web.mapper;

import lsl.java.web.entity.Depository;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
            @Result(column = "depository_name",property = "depositoryName"),
            @Result(column = "create_date",property = "createDate")
    })
    Depository getDepositoryByBookId(long bookId);


    @Update("update depository set count=#{count} where book_id=#{bookId}")
    int updateDepositoryByBookId(long bookId,int count);

    @Select("select distinct shop_id,shop_name,shop_head_img,create_date,depository_name from depository where shop_id=#{shopId}")
    @Results(id = "uniqueDepository",value = {
            @Result(column = "shop_id",property = "shopId"),
            @Result(column = "shop_name",property = "shopName"),
            @Result(column = "shop_head_img",property = "shopHeadImg"),
            @Result(column = "create_date",property = "createDate"),
            @Result(column = "depository_name",property = "depositoryName")
    })
    List<Depository> getDepositoryListByShopId(int shopId);

    @Select("select * from depository where shop_id=#{shopId} and depository_name=#{depositoryName}")
    @ResultMap("depository")
    List<Depository> getDepositoryListByShopIdAndDepositoryName(int shopId,String depositoryName);

    @Select("select * from depository where id=#{depositoryId}")
    @ResultMap("depository")
    Depository getDepositoryByDepositoryId(long depositoryId);

    @Update("update depository set count=#{count} where id=#{depositoryId}")
    int updateDepositoryCountByDepositoryId(int count,long depositoryId);


    @Insert("insert into depository(book_id,shop_id,shop_name,shop_head_img,count,create_date,depository_name) " +
            "values(#{bookId},#{shopId},#{shopName},#{shopHeadImg},#{count},#{createDate},#{depositoryName})")
    int insertOneDepository(Depository depository);


}
