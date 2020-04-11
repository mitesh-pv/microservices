package com.example.eurekaclientratingsdataservice.eurekaclientratingsdataservice.resource;

import com.example.eurekaclientratingsdataservice.eurekaclientratingsdataservice.model.Rating;
import com.example.eurekaclientratingsdataservice.eurekaclientratingsdataservice.model.UserRating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsDataServiceController {


    @RequestMapping("movies/{movieId}")
    public Rating getRating(@PathVariable("movieId") String movieId){
        return new Rating(movieId, 4);
    }

    @RequestMapping("users/{userId}")
    public UserRating getUserRating(@PathVariable("userId") String userId){
        UserRating userRating = new UserRating();
        userRating.initData(userId);
        return userRating;
    }
}
