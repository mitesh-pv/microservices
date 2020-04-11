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
