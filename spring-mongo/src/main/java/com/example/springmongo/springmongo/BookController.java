package com.example.springmongo.springmongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.InvalidMongoDbApiUsageException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLClientInfoException;
import java.sql.SQLDataException;
import java.util.List;

@RestController
@EnableRetry
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Retryable(
            value = {InvalidMongoDbApiUsageException.class, SQLClientInfoException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 10000)
    )
    @GetMapping(value = "/getBook")
    public List<Book> getBook(@RequestParam(value = "id") String id){

        int id_ = Integer.parseInt(id);
        return bookRepository.findAll();
    }

    @PostMapping(value = "/insertBook")
    public void insertBook(@RequestBody CreateRequest createRequst){

        bookRepository.save(new Book(createRequst.getId(), createRequst.getBookName(), createRequst.getAuthorName(), createRequst.getCost()));
    }

    @DeleteMapping(value = "/deleteBook")
    public void deleteBook(@RequestParam(value = "id") String id){
        int id_ = Integer.parseInt(id);
        bookRepository.deleteById(id_);
    }

    @Recover
    private String recover(SQLDataException s){
        return "You have some error occured";
    }

}
