FROM openjdk:18-slim as builder
WORKDIR application
ADD target/authentication-service-0.0.1-SNAPSHOT.jar ./
RUN java -Djarmode=layertools -jar authentication-service-0.0.1-SNAPSHOT.jar extract

FROM openjdk:18-slim
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Djava.security.edg=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]