FROM eclipse-temurin:17-jdk-alpine as builder

WORKDIR /app

COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw mvnw

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar /app/my-products-api.jar

ENV PORT=8080
EXPOSE ${PORT}
ENTRYPOINT ["java", "-jar", "/app/my-products-api.jar"]
