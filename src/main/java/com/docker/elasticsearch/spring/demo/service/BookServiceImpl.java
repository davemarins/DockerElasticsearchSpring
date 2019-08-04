package com.docker.elasticsearch.spring.demo.service;

import com.docker.elasticsearch.spring.demo.model.Book;
import com.docker.elasticsearch.spring.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book save(Book book) {
        return this.bookRepository.save(book);
    }

    public void delete(Book book) {
        this.bookRepository.delete(book);
    }

    public Book findOne(String id) {
        return this.bookRepository.findById(id).get();
    }

    public Iterable<Book> findAll() {
        return this.bookRepository.findAll();
    }

    public Page<Book> findByAuthor(String author, PageRequest pageRequest) {
        return this.bookRepository.findByAuthor(author, pageRequest);
    }

    public List<Book> findByTitle(String title) {
        return this.bookRepository.findByTitle(title);
    }

}
