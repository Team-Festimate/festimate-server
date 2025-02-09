FROM eclipse-temurin:17

WORKDIR /app

COPY build/libs/festimate-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]
