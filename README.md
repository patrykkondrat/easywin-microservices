Easywin Microservices App
=========================
## Description
This microservice backend app is designed for online betting. 
Users can place bets in the form of tickets via the Ticket Service. 
The Bet Service stores all bets and ensures that only valid bets are accepted. 
The Wallet Service manages user balances. 
Notifications are sent to users via email through the Notification Service, 
which also connects with the Ticket Service and Promo Service through Apache Kafka. 
User authorization is handled by the Keycloak Auth Service, and requests can be traced using the Zipkin Service. 
The app's performance is monitored through Prometheus and visualized using Grafana. 
All services are connected and unified through the API Gateway and Discovery Server, 
and communication between (ticket and promo) services is facilitated by the Kafka Service.
Additionally, the app is secured by HTTPS and uses OAuth2 for authentication.



## Run
### Docker
```docker-compose
sudo docker compose up -d
```

### Local
Two ways to run the app locally:
* Run docker compose with `docker-compose-local.yml` file
```docker-compose
sudo docker compose --file docker-compose-local.yml up -d
```
* Run all services in one IDE (e.g. IntelliJ IDEA)


### Services (Functionality in points)
- api `gateway`
  * Authorize endpoints by SecurityFilterChain
  * gateway for all services
- ticket service
  * Place ticket by users
  * store of all tickets (`POSTGRES/MYSQL`)
  * `Circuit breaker` on placeTicket()
  * `Async`
  * send info to wallet service about what sum should be taken from user wallet
- bet service
  * `CRUD` func for bets
  * store of all bets (`MONGO DB cloud`)
  * Change bet status send to ticket (`kafka`) info about some bets are settled
- wallet service
  * `CRUD` func for wallets
  * store of all wallets (`POSTGRES/MYSQL`)
- `discovery` server
  * `load balancing`
  * service register
- notification service 
  * `mail sender` for users who place ticket or should see a promo mail by `Thymeleaf` and `JavaMailSender`
- `keycloak` auth service
  * `Authenticate` endpoints
- `zipkin` service
  * `tracing` service
- `prometheus` service
  * `monitoring` service
- `grafana` service
  * `visualize` service
- `kafka` service
  * `message broker` service
- `zookeeper` service





[//]: # (### Impl)
[//]: # (* ticket service )
[//]: # (  * place ticket by users)
[//]: # (  * `resilience4j circuitbreaker`)
[//]: # (  * `actuator`)
[//]: # (* bet service )
[//]: # (  * store of all bets)
[//]: # (* wallet service )
[//]: # (  * balance and service of wallets)
[//]: # (* chat service )
[//]: # (  * contact with stuff)
[//]: # (* discovery server )
[//]: # (  * registration of services)
[//]: # (  * save the previous state when server is down)
[//]: # (  * load balance send request to router &#40;`discovery server`&#41; and forwards request to avaiable service instance )
[//]: # (* notification service)
[//]: # (  * communicate with promo ticket service &#40;ticket placement&#41;)
[//]: # (  * email sender)
[//]: # (* api gateway )
[//]: # (  * "unification path" for all services, )
[//]: # (  * connecting https to api gateway, when other services communicate by http)
[//]: # (* `keycloak` api auth service)
[//]: # (* `zipkin`)


[//]: # (### Future ideas)
[//]: # (* statistic/analyse service)
[//]: # (* auth service)
[//]: # (  * 3 types of accounts: user, stuff, admin)
[//]: # (  * user age verification)
[//]: # (* promo service)
[//]: # (  * promo and bonus management)
[//]: # (* payment service)
[//]: # (  * will communicate with wallet)
[//]: # (  * use only by client-bank-server side, wallet is another case)


[//]: # (-> Mini Frontend? \)
[//]: # (-> think about should microservices has shared databases \)
[//]: # (-> use SSL in microservice communication \)
[//]: # (-> read about vault \)
[//]: # (-> rate limiting on gateway !! \)
[//]: # ()
