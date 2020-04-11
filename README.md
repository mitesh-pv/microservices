# microservices
Basic implementation of microservices using spring boot

1. Communication and Discovery Servers
2. Fault tolerance and Resilience


## Fault Tolerance

### Method to solve the slowing issue

* Suppose a service slows down.
All the dependent and independent systems also slows down because of the inactive threads in the WebServers thread pool.
* Since WebServers follows thread per request model, so the threads waiting for the response from slow services stacks up on the thread pool.
This is the reason that further requests also slows down, because of not being able to create further thread.
* Temporary solution to this can be using Timeout methods. Setting up timeout for slow threads.

```java
	
	@LoadBalanced // gives a hint to discovery server about what service is to be called
	@Bean
	public RestTemplate getRestTemplate(){

		// return new RestTemplate();

		/**
		 * setting connection timeout for fault tolerance
		 */

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(3000);
		return new RestTemplate(clientHttpRequestFactory);
	}
```

#### Best solution - **Circuit Breaker Pattern** 
* detect something is wrong
* take temporary steps to avoid getting the situation worse. (if some service doestn't responds, stop sending requests for a while.
* Deactivate the problem so that it does not effect the downstream components.

#### Decisions upon which circuit triping depends.
1. Last n request to be condidered for the decision.
2. How many of those should fail?
3. Timeout duration.

#### When doest circuit gets back to normal?
1. How long to wait after the circuit breaks to try again?

### We need to do fallback if a circuit breaks
* Throw an error.
* Return a fallback default response.
* Save previous response in a cache and use that when possible.

### Circuit breakers are required
* Failing fast.
* Having fallback functionality.
* Automatic recovery.

### To write a fallback mechanism, we need to write the multithreading and concurrency programs which is complicated and tidious. 
**Hystrix** is a library that solves this problem.  
Hystrix is a Netflix library, and works very well with spring boot.  
It is not longer under development by Netflix.  

* Add dependency - spring-cloud-starter-starter-netflix-hystrix.  
* Add annotation @EnableCircuitBreaker to the application.  
* Add @HystrixCommand to methods that need circuit breaker.  
* Configure Hystrix behaviour.

```java
    @RequestMapping("/{userId}")
    @HystrixCommand(fallbackMethod = "getFallbackCatalog")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){}
```

Fallback method should be simple hadcoded response or taken from the cache with minimum possibility of getting faulty.  

Hystrix wraps the API class inside a Proxy class.   
Hence when this method is invoked, instance of a proxy class is returned instead of the API class.  
This proxy class contains the Circuit Breaker logic.  

### Problems with Hystrix proxy
If from several api calls, one of them is failing, then whole method is proxied by the fallback method.  
Hence we need to improve the  granularity in our api calling mechanism along with fallback mechanism.  

```java
    @RequestMapping("/{userId}")
    @HystrixCommand(fallbackMethod = "getFallbackCatalog")
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
                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
                }).collect(Collectors.toList());


    }
```

This can be granularated into below methods.  

```java
    @RequestMapping("/{userId}")
    @HystrixCommand(fallbackMethod = "getFallbackCatalog")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        // RestTemplate restTemplate = new RestTemplate();   => needs to be created as a bean and do the dependency injection

        // get all rated movie ids
        UserRating userRating = getUserRating(userId);

        return userRating.getUserRating().stream()
                .map(rating -> getCatalogItemRating(rating))
                .collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "getFallbackUserRating")
    private UserRating getUserRating(String userId) {
        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/"+userId, UserRating.class);
    }

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    private CatalogItem getCatalogItemRating(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }
```
### Setting up rest of the Hystrix properties (timeout, no-of-requests-under-consideration etc...)

```java
    @HystrixCommand(fallbackMethod = "getFallbackUserRating",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            }
    )
    public UserRating getUserRating(String userId) {
        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/"+userId, UserRating.class);
    }
```

### Setting up the Hystrix Dashboard

1. **Add dependency** - spring-cloud-starter-netflix-hystrix-dashboard
		   spring-boot-starter-actuator
2. **Add Annotation to application class** - @EnableHystrixDashboard
3. **Add to the application.properties file** - management.endpoints.web.exposure.include=hystrix.stream

#### Hystrix dashboard can be seen in the link - localhost:port/hystrix


  
