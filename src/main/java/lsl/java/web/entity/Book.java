package lsl.java.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private long id;
    private int channelId;
    private String name;
    private String author;
    private int price;
    private int discountPrice;
    private String content;
    private String category;
    private String imgs;
    private String pressDate;
    private String printDate;
    private String press;//出版社
    private String oneType;//一级分类
    private String twoType;//二级分类
    private int pageCount;//页数
    private int words;//字数
    private String ISBN;
    private int saleCount;//销售量
}
