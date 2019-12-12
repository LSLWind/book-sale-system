package lsl.java.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private long id;
    private long bookId;
    private long customerId;
    private String customerName;
    private String customerHeadImg;
    private String date;
    private String content;
}
