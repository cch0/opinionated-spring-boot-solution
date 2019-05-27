# Opinionated Spring Boot Solutions To Common Concerns In Distributed System

## Goal

The goal of this repository is to illustrate how to solve common concerns a typical application would face in a distributed environment by using open source solutions with little or no configurations.

The focus is on using open source libraries that are developed to work with Spring Boot application such that the same solution can be applied to other Spring Boot applications with little or no effort. Developers can then focus on application and business logic. 

### What is Being Addressed?

* request and response logging
* distributed tracing
* API endpoint client
* service discovery (TBD)
* configuration management (TBD)
* secret management (TBD)

### What Is Not Being Addressed?

The solutions described here are not specifically for running application in the Cloud environment and/or in Kubernetes cluster. To take advantage of Cloud and/or Kubernetes, a different set of solutions are recommended and is covered in `yet-to-defined` repository.

### But Opinionated?

Every problem can likely be solved differently and this is a good thing. The goal is not to convince anyone this solution is better or worse than any other solution but rather seves the purpose of providing a working solution. As technology evolves, what is considered a best/better solution is going to evolves as well. In other words, the opinionated solution provided here likely has an expiration date associated with it.


## Contrived Example

In this repo, ideas and solutions are illustrated by applying them to a contrived example. There is not much real business related functionality implemented and the idea of the example is to have multiple services working together to loosely reflect a real world scenario.



## 1. Request & Response Logging

[Logbook](https://github.com/zalando/logbook) is a Java library for HTTP request and response logging. The features it supports and flexibility it provides makes it a good solution to be included.

### Features

* Logging of HTTP request and response, including body, partial body or none at all.
* Different logging format: json, http, curl, etc
* Obfuscation of sensitive data
* Customization: including inclusion and/or exclusion of endpoints and others
* Auto configuration for Spring Boot application

### Configuration

**`pom.xml`**
```
<dependency>
    <groupId>org.zalando</groupId>
    <artifactId>logbook-spring-boot-starter</artifactId>
    <version>${logbook.version}</version>
</dependency>
```

**Sample configuration in `application.yaml`**
```
logbook:
  format:
    style: json
  include:
    - /accounts/**
  exclude:
    - /favicon.ico
    - /actuator/**
logging:
  level:
    ROOT: INFO
    org:
      zalando:
        logbook: TRACE
```


## 2. Distributed Tracing

A common requirement for services in distributed system is the ability to trace requests sent/received between services and understand where most time is spent. This is espically important when serving a request involves multiple services and the interaction between them is too complex to comprehend easily.


[Spring Cloud Sleuth](https://github.com/spring-cloud/spring-cloud-sleuth) is a distributed tracing tool for Spring Cloud. It instruments Spring components to collect trace information and can optionally send it to a Zipkin server for visualization.

[Zipkin](https://github.com/apache/incubator-zipkin) is a distributed tracing system. It helps collecting tracing data, storing, lookup and visualization. Zipkin also provides a Spring Boot based server.



### Features of Spring Cloud Sleuth

* Trace and span IDs are added to Slf4J MDC and available in the log message.
* Timing information is available for latency analysis.
* [OpenTracing](https://opentracing.io/) compatible


### Configuration

**`pom.xml`**

```
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-zipkin</artifactId>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>     
```

**Sample configuration in `application.yaml`**
```
spring:
  application:
    name: Account
  sleuth:
    sampler:
      # demo purpose only
      probability: 1.0
  zipkin:
    # zipkin server location
    base-url: http://localhost:9411
    enabled: true
    service:
      name: account-service
```

### How Does It Work

A new Trace ID will be created automatically the first time a request is entering into the system. As part of processing the same request, a new Span ID will be created automatically every time a process is interacting with external services (including but not limited to database, queue, etc). Those trace and span information are sent over to Zipkin server for storage and analysis.  


## 3. Client For Service To Service Communication

A typical communication mechanism between services is through HTTP. For a sending side to communicate with the receiving side, there are a few options to be considered:

* [Swagger Codegen](https://swagger.io/tools/swagger-codegen/)
* Hand crafted Interface class by the sending service.
* Model classes packaged in a module or library provided by the receiving service.
* Declarative REST Client using Spring Cloud OpenFeign


To quickly implement the communication solution, [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign) is a solution to be considered. It requires few little configuration and implementation effort. 

### Configuration

**`pom.xml`** (on the sending service side)
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>
```        

**Sample configuration in `application.yaml`** (on the sending service side)

```
#1 feign configuration
feign:
  okhttp:
    enabled: false
  httpclient:
    enabled: true
  client:
    config:
      # payment service connection configuration
      payment:
        connectTimeout: 5000
        readTimeout: 10000
        loggerLevel: full

client:
  payment:
    name: payment
    url: http://localhost:8081
```    

**Sample Spring Configuration** (on the sending service side)

```
@FeignClient(name = "payment", url = "${client.payment.url}")
public interface PaymentClient {
    // #2 endpoint declaration
    @RequestMapping(method = RequestMethod.GET, value = "/paymentInfo/accounts/{accountId}", consumes = "application/json")
    // #3 response class is defined on the sending side
    PaymentInfo getPaymentInfo(@PathVariable long accountId);
}
```

**API Endpoint Response Class** (on the sending service side)
```
// #3
public class PaymentInfo {
    long id;
    Date date;
}
```

**Note**

* **#1** receiving service configuration is defined in application.yaml file
* **#2** API endpoint definition is declared here
* **#3** API endpoint response class is defined on the sending side

### Discussion

* The solution decouples the services. Sending service does NOT need to depend on receiving service. Software release can then be done with less coupling.
* Declarative configuration is easier than code generation.
* Spring Cloud OpenFeign does depend on a Client Load Balancer solution. For it to work, an additional dependency (Ribbon) is also brought in as well. Whether or not using Client Load Balancer together in the Spring Boot application is recommended is up for debate.



## 4. Run All The Services

If you would like to build the services, a Dockerfile has been provided for each service.

Execute the following command to build container image for all services.

```
docker-compose build
```

A `docker-compose.yaml` has been provided to run all services on the host where docker and docker-compose are available.

```
docker-compose up -d
```

After all services are up and running, 

**Account Service** 

Account Service is available at `http://localhost:8080`

To retrieve account information for an account id:

```
http://localhost:8080/accounts/123
```


**Payment Service**
Payment Service is available at `http://localhost:8081`

To retrieve payment information for account of id 

```
http://localhost:8081/paymentInfo/accounts/123
```