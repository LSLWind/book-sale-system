package lsl.java.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Depository {
    private long id;
    private long bookId;
    private int shopId;
    private String shopName;
    private String shopHeadImg;
    private int count;//存货
    private String createDate;
    private String depositoryName;
}
