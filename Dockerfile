FROM openjdk:17-jdk-slim
LABEL authors="yusuf"

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
