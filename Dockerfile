FROM openjdk:8
EXPOSE 8080
ADD target/dh-docker.jar dh-docker.jar 
ENTRYPOINT ["java","-jar","/dh-docker.jar"] ["java","-jar","springsecurity-0.0.1-SNAPSHOT.jar"]