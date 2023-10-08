package com.lcwd.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

/*	@Autowired
	private RatingService ratingService;

	@Test
	void createRating(){

		Rating rating=Rating.builder().rating(10).userId("").hotelId("").feedback("Createddd usingfeign client").build();

		Rating savedRating=ratingService.createRating(rating);

		System.out.println("new rating created");

	}*/

}
