# Usar la imagen oficial de OpenJDK 18 como base
FROM openjdk:18-jdk-slim

# Establecer el directorio de trabajo en /app
WORKDIR /app

# Copiar el archivo JAR del proyecto al contenedor
COPY target/student-events-api-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que la aplicación está corriendo
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]