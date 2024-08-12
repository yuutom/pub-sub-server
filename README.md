# API Document
[api document by openapi](https://github.com/m-rec/merpay-api-java-template_J371828015/blob/master/api-document.html)
</br>**※Raw html file.**

# overview
This application is a "Pub/Sub"-like middleware service.

```
+---------+       +------------+       +-------------------+       +--------------+
|         | <---> |            | <---> |                   | <---> |              |
|  Client |       | Web Server |       | Application Server|       | In-Memory DB |
|         | <---> |            | <---> |                   | <---> |              |
+---------+       +------------+       +-------------------+       +--------------+
```

# For Phase 2
Note that at this time this application is a single server. (1~3 ms/request)</br>
Therefore, in order to meet the Phase 2 requirements, it is necessary to control the workload and manage transactions appropriately, for example by load balancing with a load balancer in the first stage.

Also note that this application is volatile because it uses an in-memory DB for data retention.</br>
To prevent message loss, consider making the DB non-volatile and redundant.
