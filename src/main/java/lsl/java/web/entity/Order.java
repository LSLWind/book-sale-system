package lsl.java.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int customerId;
    private long bookId;
    private String bookName;
    private int bookPrice;
    private int count;
    private String date;
}
