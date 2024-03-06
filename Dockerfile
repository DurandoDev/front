FROM openjdk:17-jdk

COPY target/front-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

EXPOSE 8081