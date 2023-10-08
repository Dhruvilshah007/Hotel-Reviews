package com.lcwd.user.service.external.services;


import org.springframework.cloud.openfeign.FeignClient;


//This is declarative approach used with Feign client
@FeignClient(name = "RATING-SERVICE")
public interface RatingService {


    //We made this just for testing how can we use feign client for PUT and post
    //testcase written in UserServiceApplicationTests file,Just run that test case to see output
    //get
/*


    //If we don't have any class we can use Map<String,Object> instead of Rating
    //POST
    @PostMapping("/ratings")
    Rating createRating(Rating values);

    //PUT

    @PutMapping("/ratings/{ratingId}")
    Rating updateRating(@PathVariable("ratingId") String ratingId, Rating values);

    @PutMapping("/ratings/{ratingId}")
    void deleteRating(@PathVariable String ratingId);*/
}
