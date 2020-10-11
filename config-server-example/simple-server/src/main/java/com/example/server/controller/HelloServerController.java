package com.example.server.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * "/hello" -> Sample Get End point returning a message with a random number appended
 * 
 * "/helloPost" -> A Sample Post End point returning a message with a random number appended
 * 
 * "/exceptionScenario/initiate" -> A Sample End Point throwing exception, this is to show how to handle business level failures. 
 * Ideally we should create a custom unchecked exception (say ServiceException) and throw it from here. We can then add a controller advice to have a
 * custom logic to handle service exceptions.
 */

@RestController
@RequestMapping(value = "/server")
public class HelloServerController {

    private static Logger logger = LogManager.getLogger();

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<String> sayHelloWorld() {
        logger.info(() -> "say hello");
        return new ResponseEntity<String>(
                "Hello World From Server " + String.valueOf(Math.random()), HttpStatus.OK);
    }

    @RequestMapping(value = "/helloPost", method = RequestMethod.POST)
    public ResponseEntity<String> sayHelloWorldPOST(@RequestBody Object body) {
        logger.info(() -> "say hello");
        return new ResponseEntity<String>(
                "Hello World From Server " + String.valueOf(Math.random()), HttpStatus.OK);
    }

    @RequestMapping(value = "/exceptionScenario", method = RequestMethod.GET)
    public ResponseEntity<String> serviceExceptionScenario() {
        throw new RuntimeException("Some dummy service exception...");
    }
}
