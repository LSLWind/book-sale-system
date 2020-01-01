package lsl.java.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private long id;
    private int shopId;
    private int isFinish;
    private String state;
    private int customerId;
    private long bookId;
    private String bookName;
    private int bookPrice;
    private int count;
    private String date;
}
