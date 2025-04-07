FROM openjdk:21-jdk-slim
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY build/libs/team-festimate-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar", "--spring.profiles.active=prod"]
