package lsl.java.web.service.impl;

import lsl.java.web.entity.Book;
import lsl.java.web.entity.Channel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IChannelServiceTest {

    @Autowired
    IChannelService channelService;
    @Autowired
    IBookService bookService;

    @Test
    void test(){
        Assertions.assertNotEquals(null,bookService.getRandomBookList());
        for(Book book:bookService.getRandomBookList()){
            System.out.println(book.toString());
        }
    }

}