FROM eclipse-temurin:23-jre

WORKDIR /app

# Copiar el JAR generado
COPY target/AuthService-0.0.1-SNAPSHOT.jar /app/app.jar

# Puerto expuesto
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "/app/app.jar"]