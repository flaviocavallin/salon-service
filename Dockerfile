FROM openjdk:13-jdk-alpine
EXPOSE 8080
VOLUME /tmp
ARG JAR_FILE
COPY target/*.jar /home/user/salon-server/app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=local","/home/user/salon-server/app.jar"]

