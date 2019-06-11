FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY einnovator-sample-movies-0.0.1.war app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]