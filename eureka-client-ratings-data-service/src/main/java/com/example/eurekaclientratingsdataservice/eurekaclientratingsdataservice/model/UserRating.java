package com.example.eurekaclientratingsdataservice.eurekaclientratingsdataservice.model;

import java.util.Arrays;
import java.util.List;

public class UserRating {

    private String userId;
    private List<Rating> userRating;

    public UserRating() {
    }

    public UserRating(String userId, List<Rating> userRating) {
        this.userId = userId;
        this.userRating = userRating;
    }

    public List<Rating> getUserRating() {
        return userRating;
    }

    public void setUserRating(List<Rating> userRating) {
        this.userRating = userRating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void initData(String userId) {
        this.setUserId(userId);
        this.setUserRating(Arrays.asList(
                new Rating("550", 3),
                new Rating("551", 4)
        ));
    }


}
