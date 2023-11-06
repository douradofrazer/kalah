FROM eclipse-temurin:17-jre-alpine

EXPOSE 9090

VOLUME /tmp

COPY target/*.jar kalah-app.jar

ENTRYPOINT ["java","-jar","/kalah-app.jar"]