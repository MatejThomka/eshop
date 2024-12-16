# 1. Stage: dev
FROM maven:3.9-eclipse-temurin AS dev

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

ENTRYPOINT ["mvn", "spring-boot:run"]

# 2. Stage: build
FROM dev AS build

WORKDIR /app

COPY --from=dev /app/pom.xml .
COPY --from=dev /app/src ./src

RUN mvn clean package -DskipTests

# 3. Stage: final
FROM build AS final

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080