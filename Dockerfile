FROM azul/zulu-openjdk-alpine:17-jre
WORKDIR /app

ADD build/libs/app.jar app.jar
ADD build/agent/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

EXPOSE 8081
ENTRYPOINT java -jar -javaagent:opentelemetry-javaagent.jar -Dspring.profiles.active=docker app.jar
