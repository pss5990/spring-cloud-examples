What is Ribbon?
1. Ribbon is a client side load balancer library originally built by Netflix

What is Eurkea?
1. Eureka is a service discovery mechanism library developed by Netflix
2. The eureka server is sort of a microservice which will discover the eureka clients.
3. So, both the eureka-enabled-client and eurkea-enabled-server are eureka clients which register themselves with Eureka server.

How we plan to use them?
Note that we will use the spring-cloud wrapper of these technologies rather than using them directly.
1. In this example the eureka-enabled-client will do a client side load balancing.
2. The eureka-enabled-client will interact with the Eureka discovery server to get the load balanced url of the eurkea-enabled-server.
3. Note that the eureka-enabled-client will only use the application-name or the service-name to get the actual url for the eurkea-enabled-server from eureka server.

Code Setup

- Setting Up Eureka Server
1. Add the dependency spring-cloud-starter-netflix-eureka-server to the pom file of eureka-server.
2. Add the annotation @EnableEurekaServer to Main class of the eureka-server.
3. Add property eureka.client.register-with-eureka=false and eureka.client.fetch-registry=false. This is needed so that the eureka client does not register with itself. Because technically a eureka server can also be a eureka client.

- Setting Up eurerka-enabled-server (Eureka Client)
1. Add the dependency spring-cloud-starter-netflix-eureka-client to the pom file of all the eureka clients.
2. You can add @EnableEurekaClient annotation to the main class of the eurkea-enabled-server but unlike @EnableEurekaServer it is not mandatory.

- Setting Up eureka-enabled-client (Also a eureka client)
1. Repeat the step 1 above to make the eureka-enabled-client a eureka client.
2. Now we need to consume the eurkea-enabled-server from the eureka-enabled-client in a load balanced way.
3. Add annotation @LoadBalanced to the Rest template bean. This will use Ribbon load balancer.
4. Now access change the url of the eurkea-enabled-server that client was using from localhost:8081 to the spring application name of the eureka-enabled-server

How to Test?
1. Start the eureka server application. Hit http://localhost:8761, this will show you the GUI for the Eureka server. Once we start the eureka clients they will be available here.
2. Start the eureka-enabled-client instance and check the eureka server GUI. You should see one instance of Eurkea client.
3. Try to hit the dummy/hello end point of eureka-enabled-client since there isn't any eurkea-enabled-server instance available with the eureka server the call should fail, but hyrstrix should kick in and you should get the fallback response.
4. Start two instances for eurkea-enabled-server, i.e, after starting one instance change the port in the application.yml and start another instance.
5. Check the Eurkea server GUI, you should now see 3 instances. 1 for eureka-enabled-client and 2 for eurkea-enabled-server. Again note that all these 3 instances are all eureka-clients.
6. Hit the end /dummy/hello end point of the eureka-enabled-client repeatedly and the response should be returned in a load balanced way with a round robin strategy, that is from the different eurkea-enabled-server instances. 
