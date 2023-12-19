FROM maven:3.8.3-openjdk-17 AS build
COPY . ./opt/
RUN cd opt/telegram-service && mvn install -DskipTests
RUN cd /opt/telegram-service && mvn clean package -DskipTests

FROM maven:3.8.3-openjdk-17
COPY --from=build opt/telegram-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]