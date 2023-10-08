package com.lcwd.user.service.services.impl;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.repositories.UserRepository;
import com.lcwd.user.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public User saveUser(User user) {

        //generate unique userId
        String randomUserId=UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {

        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given id is not found on server !!:"+userId));


        //fetch rating of above user from RATING SERVICE
        //http://localhost:8083/ratings/users/9796ea0a-4479-4dd5-b3e7-e4623498a0c0


        // So we need client to get data,which will call HttpServer with HTTP API
        //Ways -> RestTemplate, Fiegn Client

        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);
        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();

        //logger.info(ratingsOfUser.toString());
        //logger.info("{} ",ratingsOfUser);

        //Now we will get Hotel Details

        List<Rating> ratingList=ratings.stream().map(rating -> {
            //api call to HOTEL SERVICE to get hotel details
            //http://localhost:8082/hotels/f7641f79-1a4a-4cd3-91d9-9349a8db9957


            //getForEntity used to get more nfo about entity using RestTemplate

            //ResponseEntity<Hotel> forEntity=restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            //   Hotel hotel=forEntity.getBody();
            //  logger.info("response statusCOde:{}",forEntity.getStatusCode());

            //Using feignclient

            Hotel hotel=hotelService.getHotel(rating.getHotelId());



            rating.setHotel(hotel);
            //Set hotel to rating
            //return rating
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);
        return user;
    }
}
