package lsl.java.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    private int id;
    private String name;
    private String headImg;
    private String phoneNumber;
    private String password;
    private String address;

}
