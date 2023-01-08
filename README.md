Microservice app for betting. 

### Impl
* ticket service 
  * place ticket by users
* bet service 
  * store of all bets
* wallet service 
  * balance and service of wallets
* chat service 
  * contact with stuff
* discovery server 
  * registration of services
  * save the previous state when server is down
* api gateway 
  * "unification path" for all services, 
  * connecting https to api gateway, when other services communicate by http

### Future ideas
* statistic/analyse service
* auth service
  * 3 types of accounts: user, stuff, admin
* promo service
  * promo and bonus management
* payment service
  * will communicate with wallet
  * use only by client-bank-server side, wallet is another way
* notification service
  * communicate with promo (send promo materials), auth service (registration confirmation), and ticket service (ticket placement)


-> Mini Frontend?