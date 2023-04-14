![banner](https://user-images.githubusercontent.com/72699445/232042925-7852a862-304a-4731-827e-271326f45354.png)

The project is a messenger application with a simple console client that visualizes how the app works. The backend is written in Java Spring Boot framework and it has a microservice architecture. There are 3 microservices (data access, conversation, authentication) that are cooperating with each other.

![messenger-microservice-architecture](https://user-images.githubusercontent.com/72699445/232036237-0547633d-2661-4ce1-b74a-d624978f17e7.png)

## Motivation

The project was built to learn how to create microserivce application using the Spring Boot framework.

## Build Status

1. The back-end is a ready-to-use system however, there are some things that may be improved:
- there is a missing security configuration for CORS and CSRF.
- the interactions between services may be improved and more optimized
- there is no message encoding so the encrypting mechanism may be implemented (eg. hybrid encryption: RSA/ElGamal + AES/Twofish/Serpent)
2. The front-end is just a simple console application. This may be improved by creating a better front-end application (eg. Angular)

## Tech/Framework used

This project uses:
- Java 17
- Spring Boot Framework 3.0.0
- Lombok, JJWT, JAXB

## Features

The main feature of the application is its security. The JSON Web Token guarantees that the user is properly recognized and verified by the system. Moreover, the communication between services is also secured by the JWTs being replaced every 10 seconds.

## Installation

1. The first step is to clone this repository:
```
mkdir Messenger
cd Messenger
git clone https://github.com/kbarszczak/Messenger_web .
```

2. The next step is to install all dependencies and create jar files for microservices:
```
mvn clean install
```

3. Now we are ready to setup the docker image for each service:
```
docker build -t auth:latest AuthenticationService/
docker build -t conversation:latest ConversationService/
docker build -t data-access:latest DataAccess/
```

4. After the images are properly created we will create a docker container that runs all required services (also the mongodb):
```
docker compose up -d
```
From now on each microservice is setup and runs on its port:
- auth: 8090
- data-access: 8091
- conversation: 8092
- mongo-express: 8081

## How to Use?

After the installation process we are ready to run the console application by:
```
java -jar Client\target\client.jar
```
Once the application stars we are ready to use the system to send messages between users.

## Contribute
- clone the repository
- either make the changes or implement todos
- create the pull request with a detailed description of your changes
