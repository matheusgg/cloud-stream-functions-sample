# Spring Cloud Stream Functions Sample
#### This is a simple project using [Spring Cloud Stream](https://cloud.spring.io/spring-cloud-static/spring-cloud-stream/3.0.0.RC1/reference/html/index.html) with [Spring Cloud Functions](https://cloud.spring.io/spring-cloud-static/spring-cloud-function/3.0.0.RC1/reference/html/).  

*To run the application*

1. Run docker-compose
``` 
docker-compose up
``` 

2. Build the project
``` 
./gradlew clean build 
``` 

3. Run the Main class
``` 
./gradlew bootRun --args='--spring.profiles.active=routing'
``` 