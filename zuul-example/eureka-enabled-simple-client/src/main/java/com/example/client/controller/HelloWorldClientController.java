package com.example.client.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * "/hello" -> Sample Get End point, which when called will call another
 * Micro-service(MS) and return whatever the response is
 *
 * But now this has hystrix enabled. So the fallback method will be called in
 * case of an exception from the sayHelloWorld method. Also note that the
 * hystrix configuration below is a simple one, we can tune it to our needs.
 * 
 * 
 * ##### Load Balancing (Using Ribbon) Changes ##### 
 * 1. The first change we did
 * was to create a bean of Rest template rather than creating a new object each
 * time, Its declaration is now moved to MainApplication and we have annotated
 * it with @LoadBalanced annotation.
 * 
 * 2. We no longer need the exact server url. All we need is the server's
 * application name which is registered with eureka-server. So we changed the
 * property value in the application.yml from
 * http://localhost:8081/server/hello to http://simple-server/server/hello
 * 
 * See how we didnt even specify the port, just the service name registered with
 * Eureka Server.
 */
@RestController
@RequestMapping(value = "/dummy")
public class HelloWorldClientController {

	@Value("${server.url}")
	private String serverURL;

	@Autowired
	private RestTemplate restTemplate;

	private Logger logger = LogManager.getLogger();

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	@HystrixCommand(fallbackMethod = "fallbackMethod")
	public ResponseEntity<String> sayHelloWorld() {
		logger.info("Server Url->{}", () -> serverURL);
		ResponseEntity<String> re = restTemplate.getForEntity(serverURL, String.class);
		return new ResponseEntity<String>("Message from client is -> "+re.getBody(), HttpStatus.OK);
	}

	public ResponseEntity<String> fallbackMethod() {
		return new ResponseEntity<String>("I am a fallback message using hstrix", HttpStatus.OK);
	}
}
