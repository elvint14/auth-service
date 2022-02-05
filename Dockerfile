FROM openjdk:11
ADD build/libs/auth-service-1.jar auth-service-1.jar
ENTRYPOINT ["java", "-jar","auth-service-1.jar"]
EXPOSE 8085