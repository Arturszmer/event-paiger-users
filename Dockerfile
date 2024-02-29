FROM openjdk:17-jdk

WORKDIR /app

COPY event-paiger-users-web/event-paiger-users-web-0.0.1-SNAPSHOT.jar /app/event-paiger-users-web-0.0.1-SNAPSHOT.jar

EXPOSE 31002

CMD ["java", "-jar", "event-paiger-users-web-0.0.1-SNAPSHOT.jar"]
