package com.example.eurekaclientmovieinfoservice.eurekaclientmovieinfoservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.eurekaclientmovieinfoservice.eurekaclientmovieinfoservice.model.Movie;

@RestController
@RequestMapping("movies")
public class MovieInfoServiceController {

    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId){
        return new Movie(movieId, "Test Name");
    }
}
