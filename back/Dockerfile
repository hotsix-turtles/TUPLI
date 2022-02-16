FROM openjdk:11-jdk

WORKDIR /app

COPY build/libs/*.jar application.jar

EXPOSE 8080

CMD ["java", "-jar", "application.jar"]