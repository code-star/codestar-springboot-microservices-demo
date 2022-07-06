FROM openjdk:18-slim

ENV JAVA_OPTS " -Xms512m -Xmx512m -Djava.security.edg=file:/dev/./urandom"

WORKDIR application

COPY ../target/authentication-service-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["java", "-jar", "authentication-service-0.0.1-SNAPSHOT.jar"]