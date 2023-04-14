FROM openjdk:17
EXPOSE 8092
ADD target/conversation-service.jar conversation-service.jar
ENTRYPOINT ["java", "-jar", "/conversation-service.jar"]