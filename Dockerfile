FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY bfhl-api.jar app.jar
EXPOSE 8080
ENV PORT=8080
ENTRYPOINT ["java", "-jar", "app.jar"]
