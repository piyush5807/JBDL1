package com.example.gWallet.Repository;

import com.example.gWallet.Model.User;
import com.example.gWallet.Model.Wallet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class WalletDao {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RedisTemplate redisTemplate;
    private static final String KEY = "wallet";
    public Boolean updateWallet(Wallet wallet) {
        try {
            Map userHash = new ObjectMapper().convertValue(wallet, Map.class);
            redisTemplate.opsForHash().put(KEY, wallet.getUid(), userHash);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Wallet getWallet(Integer uid) {

        String id = Integer.toString(uid);
        Map userMap = (Map) redisTemplate.opsForHash().get(KEY, id);
        Wallet wallet;

        if(userMap==null || userMap.equals(null) || userMap.isEmpty())
        {
            System.out.println("Fetching from DB");
             wallet = new Wallet();
            User user = userRepository.findById(uid)
                    .orElseThrow(() -> {System.out.println("User Not Found");
                        return null;
                    });
            wallet.setAmount(user.getBal());
            wallet.setUid(user.getId());
            Map userHash = new ObjectMapper().convertValue(wallet, Map.class);
            redisTemplate.opsForHash().put(KEY, Integer.toString(wallet.getUid()), userHash);
        }
        else {
            System.out.println("Fetching from Cache");

            wallet = new ObjectMapper().convertValue(userMap, Wallet.class);
        }
        return wallet;
    }
}
