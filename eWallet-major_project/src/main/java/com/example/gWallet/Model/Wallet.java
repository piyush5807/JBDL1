package com.example.gWallet.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Wallet")
public class Wallet implements Serializable {
    @Id
    private int uid;
    private int amount;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Wallet() {
    }

    public Wallet(int uid, int amount) {
        this.uid = uid;
        this.amount = amount;
    }
}
