FROM java:8-jdk-alpine
copy target/springsecurity-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch springsecurity-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","springsecurity-0.0.1-SNAPSHOT.jar"]