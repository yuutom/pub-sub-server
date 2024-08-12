# API Document
[api document by openapi](https://github.com/m-rec/merpay-api-java-template_J371828015/blob/master/api-document.html)
</br>**※Raw html file.**

# overview
This application is a "Pub/Sub"-like middleware service.

```
+---------+       +------------+       +-------------------+       +--------------+
|         | <---> |            | <---> |                   | <---> |              |
|  Client |       | Web Server |       | Application Server|       | DB Server    |
|         | <---> |            | <---> |                   | <---> |              |
+---------+       +------------+       +-------------------+       +--------------+
```

# For Phase 2
Note that at this time this application is a single server. (3~5 ms/request)</br>
Therefore, in order to meet the Phase 2 requirements, it is necessary to control workloads and manage transactions appropriately by load balancing with load balancers, and by making each server a redundant configuration.

# Debug
1. clone this repository
2. run `docker-compose up --build` in the root directory of this project
```
merpay-api-java-template_j371828015-app-1    | 2024-08-12T04:34:14.952Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
merpay-api-java-template_j371828015-app-1    | 2024-08-12T04:34:14.958Z  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
merpay-api-java-template_j371828015-app-1    | 2024-08-12T04:34:14.959Z  INFO 1 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.26]
merpay-api-java-template_j371828015-app-1    | 2024-08-12T04:34:14.975Z  INFO 1 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
merpay-api-java-template_j371828015-app-1    | 2024-08-12T04:34:14.976Z  INFO 1 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 783 ms
merpay-api-java-template_j371828015-app-1    | 2024-08-12T04:34:14.996Z  INFO 1 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
merpay-api-java-template_j371828015-app-1    | 2024-08-12T04:34:15.044Z  INFO 1 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
merpay-api-java-template_j371828015-app-1    | 2024-08-12T04:34:15.408Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
merpay-api-java-template_j371828015-app-1    | 2024-08-12T04:34:15.417Z  INFO 1 --- [           main] com.mercari.merpay.pubsub.Application    : Started Application in 1.903 seconds (process running for 2.294)
```
3. send a request to localhost:8080
</br>ex.
```
curl -X POST \
 -H "Accept: application/json" \
 -H "Content-Type: application/json" \
 "http://localhost:8080/topic/register" \
 -d '{
    "publisherId": 1234,
    "topicName": "topic_sample"
}'
```
```
{"topicId":2,"topicName":"topic_sample"}
```
