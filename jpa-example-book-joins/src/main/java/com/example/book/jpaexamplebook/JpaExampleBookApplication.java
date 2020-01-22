package com.example.book.jpaexamplebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaExampleBookApplication implements CommandLineRunner {

	@Autowired
	private BookCategoryRepository bookCategoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(JpaExampleBookApplication.class, args);
	}

	@Override
	public void run(String...data){
//		bookRepository.save(new Book( "Intro to C++", "A", 100));
//		bookRepository.save(new Book( "Intro to C++", "B", 140));
//
//		bookRepository.save(new Book("Intro To java", "A", 123));

		bookCategoryRepository.save(new BookCategory("Programming Langs", new Book("Intro To Java",
				"A", 120), new Book("Hello Python", "B", 150)));
	}

}
