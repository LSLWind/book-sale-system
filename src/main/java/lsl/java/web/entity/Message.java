package lsl.java.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private long id;//大数据时这里的类型应该为BigInteger
    private int customerId;
    private int shopId;
    private String content;
    private String date;
    private byte state;//1表示未读消息
    private byte isToShop;//1表示是从客户向商店发送消息，0则反之
}
