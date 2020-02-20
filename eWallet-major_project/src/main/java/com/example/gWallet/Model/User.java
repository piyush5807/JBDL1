package com.example.gWallet.Model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @Column(columnDefinition = "integer default 0")
    private int bal;
    private String email;
    private String mobile;

    public User(int id, String name, int bal, String email, String mobile) {
        this.id = id;
        this.name = name;
        this.bal = bal;
        this.email = email;
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public User()
    {}

    public int getId() {
        return id;
    }
    public int getBal() {
        return bal;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setBal(int bal) {
        this.bal = bal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(int id, String name, int bal) {
        this.id = id;
        this.name = name;
        this.bal=bal;
    }
}

