FROM openjdk:17-jdk-slim

WORKDIR /app

COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh

COPY target/server.jar app.jar

# Скрипт ожидания запуска Postgres, затем запуск приложения
CMD ["./wait-for-it.sh", "postgres:5432", "-t", "60", "--", "java", "-jar", "app.jar"]