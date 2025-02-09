FROM eclipse-temurin:21

WORKDIR /app

COPY ./build/libs/*.jar festimate.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "festimate.jar"]