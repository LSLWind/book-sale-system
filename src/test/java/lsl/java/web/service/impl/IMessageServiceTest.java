package lsl.java.web.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IMessageServiceTest {

    @Autowired
    IMessageService messageService;
    @Test
    void getNewMessageByCustomerIdAndShopId() {
        System.out.println(messageService.getNewMessageByCustomerIdAndShopId(1,1));
    }
}