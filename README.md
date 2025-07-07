# <img src="./src/main/resources/static/img/jwt_logo.svg" width="20"> Symmetric Springboot JWT Boilerplate 

Example to add a self-created JWT using Springframework 6/ Spring Boot 3 ending in simple method 
security for controller actions. 

## 🚀 Start

### 🐋 Docker

💼 You will need: 
* Docker according to your OS.


1) Go to project root
2) `docker compose build`
3) `docker compose up`

### 🪚 Localhost

💼 You will need: 
* Docker according to your OS
* Java 17 installed on your machine
* JAVA_HOME set in your OS. 

`mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"`

## 🍽️ Usage

1) In root folder you find User.http (Vscode). There you could test the API.

```
GET http://localhost:8080/api/auth/login HTTP/1.1
Authorization: Basic ADM1 password
```

2) You could run the test `mvn test`
