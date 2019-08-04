package com.docker.elasticsearch.spring.demo;

import com.docker.elasticsearch.spring.demo.model.Book;
import com.docker.elasticsearch.spring.demo.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DockerElasticsearchSpringApplication.class)
public class DockerElasticsearchSpringApplicationTests {

    @Autowired
    private BookService bookService;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Before
    public void before() {
        esTemplate.deleteIndex(Book.class);
        esTemplate.createIndex(Book.class);
        esTemplate.putMapping(Book.class);
        esTemplate.refresh(Book.class);
    }

    @Test
    public void testSave() {
        Book book = new Book("1", "Elasticsearch Basics", "Davide Marino", "04-08-2018");
        Book testBook = bookService.save(book);
        assertNotNull(testBook.getId());
        assertEquals(testBook.getTitle(), book.getTitle());
        assertEquals(testBook.getAuthor(), book.getAuthor());
        assertEquals(testBook.getReleaseDate(), book.getReleaseDate());
    }

    @Test
    public void testFindOne() {
        Book book = new Book("1", "Elasticsearch Basics", "Davide Marino", "04-08-2018");
        bookService.save(book);
        Book testBook = bookService.findOne(book.getId());
        assertNotNull(testBook.getId());
        assertEquals(testBook.getTitle(), book.getTitle());
        assertEquals(testBook.getAuthor(), book.getAuthor());
        assertEquals(testBook.getReleaseDate(), book.getReleaseDate());
    }

    @Test
    public void testFindByTitle() {
        Book book = new Book("1", "Elasticsearch Basics", "Davide Marino", "04-08-2018");
        bookService.save(book);
        List<Book> byTitle = bookService.findByTitle(book.getTitle());
        assertThat(byTitle.size(), is(1));
    }

    @Test
    public void testFindByAuthor() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book("1", "Elasticsearch Basics", "Davide Marino", "04-08-2018"));
        bookList.add(new Book("2", "Apache Lucene Basics", "Davide Marino", "05-08-2018"));
        bookList.add(new Book("3", "Apache Solr Basics", "Davide Marino", "06-08-2018"));
        bookList.add(new Book("7", "Spring Data + ElasticSearch", "Davide Marino", "10-08-2018"));
        bookList.add(new Book("8", "Spring Boot + MongoDB", "Philip Orla", "11-08-2018"));
        for (Book book : bookList) {
            bookService.save(book);
        }
        Page<Book> byAuthor = bookService.findByAuthor("Davide Marino", new PageRequest(0, 10));
        assertThat(byAuthor.getTotalElements(), is(4L));
        Page<Book> byAuthor2 = bookService.findByAuthor("Philip Orla", new PageRequest(0, 10));
        assertThat(byAuthor2.getTotalElements(), is(1L));
    }

    @Test
    public void testDelete() {
        Book book = new Book("1", "Elasticsearch Basics", "Davide Marino", "04-08-2018");
        bookService.save(book);
        bookService.delete(book);
        Book testBook = bookService.findOne(book.getId());
        assertNull(testBook);
    }

}
