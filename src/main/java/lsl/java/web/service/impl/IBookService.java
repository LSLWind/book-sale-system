package lsl.java.web.service.impl;

import lsl.java.web.entity.Book;
import lsl.java.web.mapper.BookDAO;
import lsl.java.web.service.BookService;
import org.apache.ibatis.annotations.Result;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class IBookService implements BookService {

    @Resource
    private BookDAO bookDAO;

    @Override
    public List<Book> getRandomBookList(){
        return bookDAO.getRandomBookList();
    }

    @Override
    public List<Book> getBookListByChannelId(int channelId){
        return bookDAO.getBookListByChannelId(channelId);
    }

    @Override
    public Book getBookById(long id){
        return bookDAO.getBookById(id);
    }

    @Override
    public void insertOneBook(Book book){
         bookDAO.insertOneBook(book);
    }
}
