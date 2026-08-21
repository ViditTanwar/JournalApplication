FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/JournalApp-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_DATA_MONGODB_URI=${SPRING_DATA_MONGODB_URI}

ENTRYPOINT ["java","-jar","app.jar"]