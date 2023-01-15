Microservice app for betting. 

### Impl
* ticket service 
  * place ticket by users
  * `resilience4j circuitbreaker`
  * `actuator`
* bet service 
  * store of all bets
* wallet service 
  * balance and service of wallets
* chat service 
  * contact with stuff
* discovery server 
  * registration of services
  * save the previous state when server is down
  * load balance send request to router (`discovery server`) and forwards request to avaiable service instance 
* api gateway 
  * "unification path" for all services, 
  * connecting https to api gateway, when other services communicate by http
* `keycloak` api auth service
* `zipkin`


### Future ideas
* statistic/analyse service
* auth service
  * 3 types of accounts: user, stuff, admin
  * user age verification
* promo service
  * promo and bonus management
* payment service
  * will communicate with wallet
  * use only by client-bank-server side, wallet is another case
* notification service
  * communicate with promo (send promo materials), auth service (registration confirmation), and ticket service (ticket placement)


-> Mini Frontend? \
-> think about should microservices has shared databases \
-> use SSL in microservice communication \
-> read about vault \
-> rate limiting on gateway !! \
-> change WebSecurityConfigAdapter (deprecated) to SecurityFilterChain!

