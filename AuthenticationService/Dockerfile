FROM openjdk:17
EXPOSE 8090
ADD target/auth-service.jar auth-service.jar
ENTRYPOINT ["java", "-jar", "/auth-service.jar"]