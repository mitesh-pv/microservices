package com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.resource;


import com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.model.CatalogItem;
import com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.model.Movie;
import com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogService {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        // RestTemplate restTemplate = new RestTemplate();   => needs to be created as a bean and do the dependency injection

        // get all rated movie ids
        UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/"+userId, UserRating.class);

        return userRating.getUserRating().stream()
                // for each movie id call movie info service and get details
                .map(rating -> {
                     Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
                    /* Movie movie = webClientBuilder.build()
                            .get()
                            .uri("http://localhost:8090/movies/"+rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();
                    // bodyToMono ==> reactive programming in which in future data is going to be loaded
                     */
                    // put them all together
                    return new CatalogItem(movie.getName(), "desc", rating.getRating());
                }).collect(Collectors.toList());


    }
}
