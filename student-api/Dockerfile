FROM adoptopenjdk/openjdk11:jdk-11.0.2.9-slim
MAINTAINER Mahesh Gummaraju

COPY target/student-api-0.0.1-SNAPSHOT.jar student-api.jar
ENV PORT 8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/student-api.jar"]
