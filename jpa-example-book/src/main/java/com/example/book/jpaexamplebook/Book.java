package com.example.book.jpaexamplebook;

import javax.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String name;

    @Column(name = "author")
    String authorName;
    int cost;

    public Book(){
    }

    public Book(String name, String authorName, int cost){
        this.name = name;
        this.authorName = authorName;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString(){
        return "name = " + this.name + " authorName = " + this.authorName + " cost = " + this.cost;
    }
}
