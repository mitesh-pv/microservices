package com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.service;

import com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.model.CatalogItem;
import com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.model.Movie;
import com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.model.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    public CatalogItem getCatalogItemRating(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }


    private CatalogItem getFallbackCatalogItem(Rating rating){
        return new CatalogItem("No movies", "", rating.getRating());
    }

}
