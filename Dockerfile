FROM openjdk:11.0.13
MAINTAINER "elvintaghiyev184@gmail.com"
ADD build/libs/auth-service-1.jar auth-service-1.jar
ENTRYPOINT ["java", "-jar","auth-service-1.jar"]
EXPOSE 8085
