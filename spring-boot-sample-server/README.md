# SpringBootServerSample
SpringBootServerSample is the sample server preresentation sample structure of project include Logging, Authentication, Configure datasource

Easy to use, and you just need to customize a few things to adapt to your business

# How to run project on your local

```bash
git clone https://github.com/sangqle/spring-boot-server-sample.git

cd spring-boot-server-sample
git checkout 0.Init-sample-server

mvn clean install
```

## For VSCode
### One of the following ways:

- In main class at <code>SpringBootServerApplication.java</code> then <strong>right-click and select 'Run Java'</strong>
- Run with command line:
  ```bash
  mvn spring-boot:run
  ```
## For IDE
...

# Testing Application

### Testing sample controller

GET: http://localhost:8080/api/all

# Project Structure
By default, Spring boot uses the embedded Tomcat server to run the application. At times, we may need to use Jetty server in place of Tomcat server.

Spring Boot bundles Tomcat and Jetty dependencies as separate starters to help make this process as easy as possible

## Add spring-boot-starter-jetty Dependency

We will need update <code>pom.xml</code> and add the dependency for spring-boot-starter-jetty. And you also need to exclude the default added <strong>spring-boot-starter-tomcat</strong>

```xml
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>2.7.4</version>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

## Override Jetty Default Configuration


<code>application.properties</code>
```code
server.port=8080
```