package com.example.ratingsdataservice.RatingsDataService.resource;

import com.example.ratingsdataservice.RatingsDataService.model.Rating;
import com.example.ratingsdataservice.RatingsDataService.model.UserRating;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsDataServiceController {


    @RequestMapping("/{movieId}")
    public Rating getRating(@PathVariable("movieId") String movieId){
        return new Rating(movieId, 4);
    }

    @RequestMapping("users/{userId}")
    public UserRating getUserRating(@PathVariable("userId") String userId){

        UserRating userRating = new UserRating();
        userRating.setUserRating(
                Arrays.asList(
                        new Rating("1234", 4),
                        new Rating("5678", 8)
                )
        );

        return userRating;

    }
}
