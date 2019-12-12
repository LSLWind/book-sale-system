package lsl.java.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private int id;
    private long bookId;
    private int customerId;
    private String bookName;
    private String bookImg;
    private int bookPrice;
    private int count;
    private String addDate;
}
