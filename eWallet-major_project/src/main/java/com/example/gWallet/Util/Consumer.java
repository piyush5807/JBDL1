package com.example.gWallet.Util;

import com.example.gWallet.Model.Transaction;
import com.example.gWallet.Model.User;
import com.example.gWallet.Repository.TransactionRepository;
import com.example.gWallet.Repository.UserRepository;
import com.example.gWallet.service.EmailService;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class Consumer {
    @Autowired
    private UserRepository repository;
    @Autowired
    private TransactionRepository trepository;
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);
    @KafkaListener(topics = "test", groupId = "group_id")
    public void consume(String id){

        logger.info(String.format("$$ -> Consumed Message -> %s",id));

        // Checking if the message is for txn History
        if(id.contains("txn")){
            sendTxnHistory(id);
            return;
        }
        int t_id = Integer.parseInt(id);
        Optional<Transaction> transaction = trepository.findById(t_id);
        Transaction transaction1 = transaction.get();
        int amt = transaction1.getAmount();
        User sender = repository.findById(transaction1.getSid())
                .orElseThrow(() -> {
                    System.out.println("User Not Found");
                    return null;
                });
        User receiver = repository.findById(transaction1.getRid())
                .orElseThrow(() -> {
                    System.out.println("User Not Found");
                    return null;
                });
        EmailService.sendEmail(sender.getEmail());
        EmailService.sendEmail(receiver.getEmail());
    }
    private void sendTxnHistory(String id) {
        String[] arrOfStr = id.split("t", 0);
        String new_id ="";
        for (String a : arrOfStr){
            new_id = a;
            break;
        }
        int id1 = Integer.parseInt(new_id);
        ArrayList<Transaction> list =
                (ArrayList<Transaction>) trepository.findBysidAndrid(id1);

        Optional<User> user = repository.findById(id1);
        User user1 = user.get();
        String filename ="test.csv";
        try {
            FileWriter fw = new FileWriter(filename);

            for(int i=0;i<list.size();i++) {
                fw.append(list.get(i).getStatus());
                fw.append(',');
                int amt = list.get(i).getAmount();
                Integer obj = amt;
                fw.append(obj.toString());
                fw.append(',');
                fw.append(list.get(i).getDate().toString());
                fw.append(',');
                int id2 = list.get(i).getId();
                Integer obj2 = id2;
                fw.append(obj2.toString());
                fw.append(',');
                int rid = list.get(i).getRid();
                obj = rid;
                fw.append(obj.toString());
                fw.append(',');
                int sid = list.get(i).getSid();
                obj = sid;
                fw.append(obj.toString());
                fw.append('\n');
            }
            fw.flush();
            fw.close();
            System.out.println("CSV File is created successfully.");
            EmailService.sendEmailWithAttachments("","",user1.getEmail(),"","","","",filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
