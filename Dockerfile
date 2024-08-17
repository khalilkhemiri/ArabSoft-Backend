FROM openjdk:17

COPY target/basic-auth-0.0.1-SNAPSHOT.jar .

EXPOSE 8083

ENTRYPOINT ["java","-jar","/basic-auth-0.0.1-SNAPSHOT.jar"]