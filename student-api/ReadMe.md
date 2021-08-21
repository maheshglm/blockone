# Spring Boot Student API

This is a sample Java/Maven/Spring Boot application for Student CRUD operations.

## How to Run

* Clone this repository
* Make sure you are using JDK 11 and Maven 3.x
* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service by one of these two methods:

```
    java -jar -Dspring.profiles.active=test target/student-api-0.0.1-SNAPSHOT.jar
or
    mvn spring-boot:run -Dspring.profiles.active=test"
```

Once the application runs you should see something like this

```
org.springframework.boot.web.embedded.tomcat.TomcatWebServer: Tomcat started on port(s): 8080 (http) with context path ''
org.springframework.boot.StartupInfoLogger: Started StudentApiApplication in 9.36 seconds (JVM running for 11.017)
com.blockone.test.studentapi.StudentApiApplication: Application started
```

## Dockerizing MySql and API

Student API uses H2 as in memory database for `test` profile, but with `dev` profile it can be integrated with mysql
database. Current project configured to use `mysql` container database.

note:Ensure `docker` is installed in your machine

### Instructions to build mysql image

* Clone this repository
* Run below commands

```
    docker build -t student-mysql-db:latest mysql/
```

### Instructions to build app image

* Clone this repository
* Run below commands in sequence

```
    mvn clean package
    docker build -t student-api:latest .
```

### Running Student-api as container with in-memory database

* Ensure `student-api:latest` image is built successfully
* confirm the image is created by running `docker images student-api:latest` (you should see a record)
* Run `docker run -d -p 8080:8080 --name=app student-api:latest`
* Verify the container is running `docker ps | grep app`
* Check logs `docker logs app -f`
* App container starts with `test` profile by default, but it can be overridden with environment variable as shown in
  below

### Running the student-api container with mysql database container

* Ensure `student-mysql-db:latest` image is built successfully
* confirm the image is created by running `docker images student-mysql-db:latest` (you should see a record)
* Run below command to spin up a container database

```
docker run -d \
    -p 3306:3306 \
    -v mysql-storage:/var/lib/mysql \
    --env=MYSQL_ROOT_PASSWORD="password"   \
    --name app-db   \
    student-mysql-db:latest
```

* Verify the container is running `docker ps | grep app-db`
* Run below command to spin up a app container

```
docker run -d   \
    -p 8080:8080    \
    --env="SPRING_PROFILES_ACTIVE=dev"  \
    --name=app  \
    --link app-db   \
    student-api:latest
```

* verify app container is started successfully `docker logs -f app`
* Run below commands to stop & remove app and app-db containers

```
  docker stop app && docker rm app
  docker stop app-db && docker rm app-db
```

### Using docker-compose to spin application stack

* To bring up the stack `docker-compose up -d`
* To bring down the stack `docker-compose down`
* To bring down the stack along with clearing persistent volume `docker-compose down --volumes`

## Student API end points

```
http://localhost:8080/fetchStudents?id=<id>
http://localhost:8080/fetchStudents?clazz=<clazz>
http://localhost:8080/addStudent
http://localhost:8080/updateStudent
http://localhost:8080/deleteStudent

```

### Create a student resource

```
POST /addStudent
Accept: application/json
Content-Type: application/json

{
	"id":3,
	"firstName": "Mahesh",
	"lastName": "G",
	"clazz":"3 C",
	"nationality": "SG"
}

RESPONSE: HTTP 201 (Created)
```

### Retrieve Student by unique id

```
GET /fetchStudents?id=3

Response: HTTP 200
```

### Retrieve Student by clazz

```
GET /fetchStudents?clazz=3 C

Response: HTTP 200
```

### Update a student resource

```
PUT /updateStudent
Accept: application/json
Content-Type: application/json

{
	"id":3,
	"nationality": "India"
}

RESPONSE: HTTP 200
```

### delete a student resource

```
PUT /deleteStudent?id=3

RESPONSE: HTTP 200
```

## To view Swagger 2 API docs

Run the server and browse to localhost:8080/swagger-ui.html

### To view your H2 in-memory datbase

The 'test' profile runs on H2 in-memory database. To view and query the database you can browse
to http://localhost:8080/h2-console. Default username is 'sa' with a blank password.

## Sonarqube integration & checks

* Download sonarqube from official website
* start the sonarqube server by executing StartSonar.bat in Windows or sonar.sh in mac
* If docker installed in your machine, run the following command to spin up a sonarqube container

```
   docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest
```

* Run

``` 
    mvn clean install -Dspring.profiles.active=test
    mvn sonar:sonar -Dsonar.login=<username> -Dsonar.password=<password>
```

* Once command is successful, you can see a message like this:

```
[INFO] ANALYSIS SUCCESSFUL, you can browse http://localhost:9000/dashboard?id=com.block-one.test%3Astudent-api
```

## Kubernetes deployment

Apologies if I am not intended to this part, but out of my interest, I have explored this section This exercise done on
the Docker Desktop with K8 installed Ensure the image is built and pushed to Docker hub (or Docker registry)

### K8 deployment with h2 database (in-memory)

* Ensure kubernetes is running on your machine.
* Open `/k8/app_test.yaml` and update image name as per the docker registry config (LN#17)
* Run `kubectl create -f app_test.yaml`
* Verify `kubectl get pods`
* Once `POD` status showing as `Running`, check API endpoint on port `127.0.0.1:30007`
* To delete the deployment `kubectl delete -f app_test.yaml`

### K8 deployment with mysql database

* Ensure kubernetes is running on your machine
* Open `/k8/db.yaml` and update image name as per the docker registry config (LN#62)
* Run `kubectl create -f db.yaml`
* Verify `kubectl get pods`
* Wait till `studentapi-db` pod gets `Running` status
* Open `/k8/app_dev.yaml` and update image name as per the docker registry config (LN#19)
* Run `kubectl create -f app_dev.yaml`
* Verify `kubectl get pods`
* Wait till `studentapi` pod gets `Running` status
* Check API endpoint on port `localhost:30007`
* To delete the deployment `kubectl delete -f db.yaml` && `kubectl delete -f app_dev.yaml`