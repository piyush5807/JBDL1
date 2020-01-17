package com.example.book.jpaexamplebook;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {

    List<Book> findByAuthorName(String a_name);

    Book findById(int id);

}
