package lsl.java.web.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private int id;
    private String name;
    private String sex;
    private String birthDate;
    private String headImg;
    private String province;
    private String city;
    private String address;
    private String email;
    private String phoneNumber;
    private String password;
    private int changes;
    private int points;
}
