package com.example.jpaspringsecurity.springsecurityjpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Autowired
    UserServiceRepository userServiceRepository;

    @GetMapping(value = "/")
    public String greetEveryone(){
        return "Hello Everyone";
    }

    @GetMapping(value = "/user")
    public String greetUsers(){
        return "Hello users";
    }

    @GetMapping(value = "/admin")
    public String greetAdmin(){
        return "Hello Admin";
    }

    @PostMapping(value = "/signup")
    public void signUp(@RequestBody SignUpRequest signUpRequest){
        String roles = "";
        for(int i=0;i<signUpRequest.getAuthorities().size() - 1; i++){
            roles += signUpRequest.getAuthorities().get(i) + ":";
        }

        roles += signUpRequest.getAuthorities().get(signUpRequest.getAuthorities().size() - 1);
        userServiceRepository.save(new MyUserDetails(signUpRequest.getName(), signUpRequest.getPassword(), true, roles));
    }
}
