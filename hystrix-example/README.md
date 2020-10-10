What is Hystrix ?
1. Open source library originally created by Netflix.
2. Implements circuit breaker pattern for fault tolerance and resilience.
3. Rather than using netflix hystrix directly we will use spring cloud hystrix wrapper/eco-system.

Steps to follow to use Spring Cloud Hystrix ?
1. Add the sping-cloud-starter-netflix-hystrix to pom.
2. Add @EnableCircuitBreaker to the application class.
3. Add @HystrixCommand to method that need circuit breaking.
4. Configure it to provide your circuit breaking criteria and alternative route

How to test ?
1. Start the client-with-hystrix either in IDE OR by running mvn spring-boot:run and try to http://localhost:8080/dummy/hello it should show you the message from hystrix because the server it tries to connect to isn't available.
2. Start the test-server and hit the same url again as in step 1 and now you should have the message from the test-server instead of the hystrix fallback message.
