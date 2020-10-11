What is Spring Cloud Config Server?
A Spring cloud config server can be used to centralize the configuration property management using a git based reporsitory. The config clients can then refer to the config server for the properties. Spring config server has support for both yaml or property file based config with support for heirarcial declaration (common as well as application name specfic property files) as well as for spring profiles.


How we plan to use it?
The config-server will connect to a config Repo hosted on Github using SSH connection. Other modes like a locally located git repo or connecting over an http is also possible. Do note that a RSA key is required when using a SSH connection for a private git repo.

Other micro-services like Eureka server, hystrix-enabled-simple-client, simple-server, zuul will connect to the config server to read properties. 

Code Setup?
1. Add the dependency spring-cloud-config-server to the config-server and the annotation @EnableConfigServer to the main class of the config-server

2. Add the dependency spring-cloud-starter-config to all other MSs. When the MSs start up they will look for the config server by default on the localhost:8888 but we added the property spring.cloud.config.uri just for reference.

3. By default the properties are only looked up during the applciation startup. To refresh the properties dynamically add the annotation @RefreshScope to the class whose properties you want to be refreshed. Only the properties declated with @Value annotation or with @ConfigurationProperty are refreshed.

4. Once the properties have bene changed in the git repo. The refresh end point of the spring actuator can be called to refresh the properties marked with @RefreshScope.

How to Test?
1. Start the config server.
2. Start the hystrix-enabled-simple-client.
3. Start the simple-server.
4. The application ports for both the client and server are pulled from config server.
5. Once everything is started up. Hit the zuul proxy end point http://localhost:8080/dummyHello/John, this will return a sample hello message, the value of the message should have been pulled from the config server and should have the text "from config server".
6. Now to test the dynamic property loading. Change the 'client.hello.name.message' property in the configurations-repo folder. Commit it and push it. Now to refresh the properties in the client make a POST request to the refresh actuator end point of the client. URL -> http://localhost:8080/actuator/refresh.

7. Request for http://localhost:8080/dummyHello/John again, you should get the new message now. 

