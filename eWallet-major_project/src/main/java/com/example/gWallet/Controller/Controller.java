package com.example.gWallet.Controller;

import com.example.gWallet.Model.AddBalanceDetails;
import com.example.gWallet.Model.Transaction;
import com.example.gWallet.Model.User;
import com.example.gWallet.Model.Wallet;
import com.example.gWallet.Repository.TransactionRepository;
import com.example.gWallet.Repository.UserRepository;
import com.example.gWallet.Repository.WalletDao;
import com.example.gWallet.Util.Producer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RestController
public class Controller {
    @Autowired
    private UserRepository repository;
    @Autowired
    private TransactionRepository trepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private WalletDao WDao;
    private static final String TOPIC = "test";
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    RedissonClient redisson = Redisson.create();

    @GetMapping("/users")
    List<User> findAll() {
        return repository.findAll();
    }

    // Save
    @PostMapping("/users")
    //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    User newBook(@RequestBody User newUser) {
        User user=  repository.save(newUser);
        /*
        Saving balance in Redis cache first, and then in DB
         */
        Wallet wallet = new Wallet();
        wallet.setAmount(user.getBal());
        wallet.setUid(user.getId());
        WDao.updateWallet(wallet);
        return user;
    }
    // Save
    @PutMapping("/users")
    User updateUser(@RequestBody User newUser) {
        User user=  repository.save(newUser);
        Wallet wallet = new Wallet();
        wallet.setAmount(user.getBal());
        wallet.setUid(user.getId());
        WDao.updateWallet(wallet);
        return user;
    }

    // Find
    @GetMapping("/users/{id}")
    User findOne(@PathVariable int id) {
        Optional<User> user= repository.findById(id);
        if(user.isPresent()){
            System.out.println("User is there");
        }
        User new_user = user.get();
        return repository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("User Not Found");
                    return null;
                });
    }

    @PostMapping("/sendMoney")
    //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    Transaction addBal(@RequestBody Transaction transaction) throws Exception {
        transaction.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        int amt = transaction.getAmount();
        User sender = repository.findById(transaction.getSid())
                .orElseThrow(() -> {
                    System.out.println("User Not Found");
                    return null;
                });
        User receiver = repository.findById(transaction.getRid())
                .orElseThrow(() -> {
                    System.out.println("User Not Found");
                    return null;
                });
        if (sender.getBal() < amt) {
            throw new Exception("Not Sufficient Balance");
        }
        sender.setBal(sender.getBal() - amt);
        Wallet senderWallet = new Wallet();
        senderWallet.setAmount(sender.getBal());
        senderWallet.setUid(sender.getId());

        receiver.setBal(receiver.getBal() + amt);
        Wallet receiverWallet = new Wallet();
        receiverWallet.setAmount(sender.getBal());
        receiverWallet.setUid(sender.getId());
        transaction.setStatus("SUCCESS");

        logger.info(String.format("$$ -> Producing Transaction --> %s", transaction));
        kafkaTemplate.send(TOPIC, Integer.toString(transaction.getId()));
        repository.save(receiver);
        repository.save(sender);
        // Updating balance in Redis Cache
        WDao.updateWallet(senderWallet);
        WDao.updateWallet(receiverWallet);
        return trepository.save(transaction);
    }

    @GetMapping("/getBal/{id}")
    int getBal(@PathVariable int id) throws Exception {
        Wallet wallet = null;
        wallet = WDao.getWallet(id);
        // If not present in the Redis then go for MySQL
        if(wallet==null){
            User user = repository.findById(id)
                    .orElseThrow(() -> {
                        System.out.println("User Not Found");
                        return null;
                    });
            if(user==null) throw new Exception("User Not Found");
            else {
                return user.getBal();
            }

        }else{
            return wallet.getAmount();
        }
    }
    @PostMapping("/addBalance")
    AddBalanceDetails addBalance(@RequestBody AddBalanceDetails request){

        System.out.println(request.toString());
        User user = repository.findById(request.getUid())
                .orElseThrow(() -> {
                    System.out.println("User Not Found");
                    return null;
                });
        Wallet wallet = new Wallet();
        wallet.setAmount(user.getBal()+request.getAmount());
        wallet.setUid(user.getId());
        user.setBal(user.getBal()+request.getAmount());
        repository.save(user);
        WDao.updateWallet(wallet);
        return request;
    }

    @GetMapping("/txnHistory/{id}")
    String getTransactionHistory(@PathVariable int id) {
        logger.info(String.format("$$ -> Producing Transaction --> %s", id));
        String new_id = Integer.toString(id)+"txn";
        kafkaTemplate.send(TOPIC, new_id);
        return "You will get the file on your email";
    }

}
