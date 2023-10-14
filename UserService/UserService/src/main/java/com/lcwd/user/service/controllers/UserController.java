package com.lcwd.user.service.controllers;


import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.services.UserService;
import com.lcwd.user.service.services.impl.UserServiceImpl;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserService userService;
    //create


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){



        User user1=userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    int retryCount=1;
    //single user get

    @GetMapping("{userId}")
//    @CircuitBreaker(name="ratingHotelBreaker",fallbackMethod = "ratingHotelFallback")
//    @Retry(name ="ratingHotelService",fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "userRateLimiter",fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){
        logger.info("UserController->getSingleUser starts");
        logger.info("Retry Count:{}",retryCount);
        retryCount++;
        User user= userService.getUser(userId);
        return ResponseEntity.ok(user);

    }



    //creating fall back method for circuit breaker.This will only be called if any(rating/hotel) service is DOWN
    //IMP point - Return type and parameters(should match) of fallback method and main method should be same.For ge in our case ResponseEntity<User>
    //For configuration we can do Java based as well as Resilence4j(yml based configuration)
    public ResponseEntity<User> ratingHotelFallback(String userId,Exception ex){
//        logger.info("Fallback is executed because service is down->"+ex.getMessage());

        User user=User.builder()
                .email("dummy@gmail.com")
                .name("dummy")
                .about("this user is created dummy because some service ism down")
                .userId("135235")
                .build();
        return  new ResponseEntity<>(user,HttpStatus.OK);
    }


    //all user get
    @GetMapping
    public ResponseEntity<List<User>> getAllUser(){

        List<User> allUser=userService.getAllUser();
        return ResponseEntity.ok(allUser);

    }
}
