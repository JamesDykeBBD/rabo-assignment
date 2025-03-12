FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B
COPY src src
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
WORKDIR /app
# Copy the JAR file directly from build stage
COPY --from=build /workspace/app/target/app.jar app.jar
HEALTHCHECK --interval=30s --timeout=3s CMD wget -q -T 3 -s http://localhost:8080/actuator/health || exit 1
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
