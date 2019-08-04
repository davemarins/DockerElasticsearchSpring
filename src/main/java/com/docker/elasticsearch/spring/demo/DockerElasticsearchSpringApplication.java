package com.docker.elasticsearch.spring.demo;

import com.docker.elasticsearch.spring.demo.model.Book;
import com.docker.elasticsearch.spring.demo.service.BookService;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class DockerElasticsearchSpringApplication implements CommandLineRunner {

    @Autowired
    private ElasticsearchOperations es;

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(DockerElasticsearchSpringApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        printElasticSearchInfo();
        bookService.save(new Book("1", "Elasticsearch Basics", "Davide Marino", "04-08-2018"));
        bookService.save(new Book("2", "Apache Lucene Basics", "Davide Marino", "05-08-2018"));
        bookService.save(new Book("3", "Apache Solr Basics", "Davide Marino", "06-08-2018"));
        Page<Book> books1 = bookService.findByAuthor("Davide", new PageRequest(0, 10));
        List<Book> books2 = bookService.findByTitle("Elasticsearch Basics");
        books1.forEach(x -> System.out.println(x));
        books2.forEach(x -> System.out.println(x));
    }

    // useful for debug, print elastic search details
    private void printElasticSearchInfo() {
        System.out.println("----- ElasticSearch -----");
        Client client = es.getClient();
        Map<String, Settings> asMap = client.settings().getAsGroups();
        asMap.forEach((k, v) -> {
            System.out.println(k + " = " + v.toString());
        });
        System.out.println("----- ElasticSearch -----");
    }

}
