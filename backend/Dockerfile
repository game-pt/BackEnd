FROM gradle:8.3-jdk17-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle gradlew gradle ./
COPY src ./src
RUN chmod +x ./gradlew
RUN gradle clean bootJar

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
