FROM openjdk:21-jdk
EXPOSE 8050
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar 
ENTRYPOINT ["java", "-jar", "/application.jar"]
