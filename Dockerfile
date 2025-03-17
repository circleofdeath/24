FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
COPY keystore.p12 keystore.p12
ENTRYPOINT ["java","-jar","/app.jar"]