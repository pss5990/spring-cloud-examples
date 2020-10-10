What is ZUUL?
1. Zuul is an API gateway developed by netflix which will receive all the incoming requests to our archhitecture.
2. Zuul proxy or simply Zuul would act as a unified front door for all our request to the backend system.
3. We will employ it to solve our cross cutting concerns like dynamic routing, monitoring, resiliency and security.
4. Zuul can be used in conjunction with other netflix libraries like Eureka, hystrix etc.
5. Zuul has 4 filters Pre, Post, Route and Error Filter.
6. As the name suggests Pre filters run before the request is routed, Post run after the request has been routed, Route filters handle the actual request routing and Error filter run if there is an error in the course of handing the request.
  
How we plan to use them?
1. Rather than hitting the eurkea-enabled-client directly, we will hit the Zuul server end point. All our requests will now go through Zuul.
2. Zuul server should route the requests to the appropriate backend MS based on the context root of the original request. This is called context based routing.
3. We will route the requests in a load balanced way and not by using the direct URLs, so Zuul server needs to register itself as a eureka client to the eureka server.
4. The logic defined in our filters should be executed. Right now we have defined just one Pre filter called Logging filter, which logs the request entry of each request to Zuul.
5. The interaction between the eurkea-enabled-client (with), eureka-server and eurkea-enabled-server is same as we saw in the previous example of clientServerWithEurekaRibbon.

Code Setup?
- Zuul Proxy
1. Add the dependency spring-cloud-starter-netflix-zuul and spring-cloud-starter-netflix-eureka-client (since we want to access the backend APIs in a load balanced way)
2. Add annotation @EnableZuulProxy to the Main Class.
3. Create a pre filter like LoggingFilter which extends ZuulFilter and also annotate the class with @Component.

- Rest of the MSs are as setup earlier

How to Test?
1. Start the Eureka-server. (Will start on 8761 as configured in the application.yaml)
2. Start the Zuul. (Will start on 9080 as configured in the application.yaml)
3. Start the eurkea-enabled-client. (Will start on 8080 as configured in the application.yaml)
4. Start 2 instances of eurkea-enabled-server. By starting one instance and then changing the server port proerty in application yaml.
5. Wait for 2 minutes, so that everything is registered correctly with eureka server. And then check the eureka server GUI on http://localhost:8761/. You should see four instances, 1 zuul, 1 eurkea-enabled-client and 2 eurkea-enabled-server.
6. According to the application.yaml config of Zuul MS, everything with that follows /app1, that is /app1/** , should go to the eurkea-enabled-client. Try to hit http://locahost:9080/app1/dummy/hello.
7. Try to hit http://localhost:9080/app2/server/hello. Since the Zuul accesses the server in a load balanced way using Ribbon, we should see the the different port numbers in each response message.
8. Check the logs for the ZUUL, there should be a log for each request that you made. Search for something like "Request recieved. Request Method -"
