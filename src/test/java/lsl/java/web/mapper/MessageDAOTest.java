package lsl.java.web.mapper;

import lsl.java.web.service.impl.IMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageDAOTest {

    @Autowired
    IMessageService messageService;
    @Test
    void getNewMessageByCustomerIdAndShopId() {
        System.out.println(messageService.getNewMessageByCustomerIdAndShopId(1,1));
    }
}