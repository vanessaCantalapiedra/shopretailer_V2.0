[![Build Status](https://travis-ci.org/vanessaCantalapiedra/shopretailer_v2.0.svg?branch=master)](https://travis-ci.org/vanessaCantalapiedra/shopretailer_v2.0)

SHOP RETAIL MANAGER  - REST API 
====================================

## Description
Very simple version of a shop retail manager API, using REST services. 

Basically it has to cover the following actions:
 - Add a new Shop : when a user POST a shop info, the shop is stored.
    * For concurrent inserts, only the first one is taken into account, the second client petition is rejected.
 - Modify an existing Shop: when a user POST an existing shop, the shop is updated. The old version of the shop is returned.
 - Get all Shops
 - Given latitude and longitude information in the url by a client , locate the shop closest to him and returning the address info of the shop.

## Requirements

 - Java 1.8
 - Gradle
 
## Usage 

#### Running the Service

```sh
./gradlew bootRun # use 'gradlew.bat bootRun' on Windows
```

#### Distributing the Service

```sh
./gradlew build # use 'gradlew.bat build' on Windows
```
generates the binary `./build/libs/shopretailer-rest-service-2.1.0.jar`

#### Testing the Service

##### Java
Some Java unit-tests and integration tests are included. To execute them, run:  
```sh
./gradlew test # use 'gradlew.bat test' on windows
```
#### CI
The project has been configured to perform Continuous Integration using Travis CI. In the top of this document the status of the build can be check, also it has the direct link to travis build page.

### Development Comments

#### Validation
I have used JSR-303 for the validation of the request parameters. I'd have liked to do more checkings and validations to avoid unexpected errors.
Also a @ControllerAdvice class has been used to treat the exceptions generated by the controllers.

#### Persistence

For this version of the application, persistence has not been implemented. A basic respository has been created with a ConcurrentHashMap that allows the basic CRUD operations. The ConcurrentHashMap is suitable for this situation , due basically to the "save" method , which returns the previous version of the object in a single synchronization call, apart from the putIfAbsent , which will atomically add a mapping if the specified key does not exist. Besides it does not lock the entire table,only the segment, what is suitable for a high rate of updates or insertions.

#### Asynchronous operations
The call to the Google Api is done in a asynchrous way, using callbacks and CompletableFuture future (JAVA 8). This way , the response  to the client does not have to wait for the answer from Google. The geolocation info will be updated in the shop when the callback is called.

#### Harvesine
The Harvesine formula is used to calculate the distance between 2 points given their latitude and longitude.

#### TODO
I'd have liked to add more tests, specially real unit tests. Also It has to be improve the error control when the user inputs data for new or old shop.
Apart from that it would be nice to have a better way to deploy it, like generating a image or docker file.
By using the concurrentHashMap makes the app difficult to scale, so the persistance layer should be implemented in another way. 

#### RANDOM COMMENTS - ANSWERING QUESTIONS

How you would expand this solution, given a longer development period?

How would you go about testing this solution?
I have created some integrating testing with mockito, basically calling for the main functions exposed by our api. 

How would you integrate this solution into an existing collection of solutions used by the Retail Manager?
Probably this solution is suitable to be implemented as a microservice, so you can integrate with the rest of the microservices of the App.

How would you go about deploying this solution to production systems?
Docker image file can be used. Also it would be nice to do this process automatically, configuring the build and including this deployment step in the CI system.

I enjoyed coding it because I had the opportunity to research and use technologies  i have never worked with, such as Google Api for example. I had fun, thanks for the challenge.
