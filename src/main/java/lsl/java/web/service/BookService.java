package lsl.java.web.service;

import lsl.java.web.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getRandomBookList();

    List<Book> getBookListByChannelId(int channelId);

    Book getBookById(long id);
}
