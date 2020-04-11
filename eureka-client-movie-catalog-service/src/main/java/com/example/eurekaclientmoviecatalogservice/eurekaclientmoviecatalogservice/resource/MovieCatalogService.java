package com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.resource;


import com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.model.CatalogItem;
import com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.model.Movie;
import com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.model.Rating;
import com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.model.UserRating;
import com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.service.MovieInfo;
import com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.service.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogService {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private MovieInfo movieInfo;

    @Autowired
    private UserRatingInfo userRatingInfo;

    @RequestMapping("/{userId}")
    // @HystrixCommand(fallbackMethod = "getFallbackCatalog")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        // RestTemplate restTemplate = new RestTemplate();   => needs to be created as a bean and do the dependency injection

        // get all rated movie ids
        UserRating userRating = userRatingInfo.getUserRating(userId);

        return userRating.getUserRating().stream()
                .map(rating -> movieInfo.getCatalogItemRating(rating))
                .collect(Collectors.toList());
    }

    public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId){
        return Arrays.asList(new CatalogItem("No movie", "", 0));
    }

}
