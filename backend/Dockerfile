FROM openjdk:10-jre-slim
COPY . /usr/src/server/
WORKDIR /usr/src/server/
EXPOSE 8443
EXPOSE 8080
EXPOSE 18443
ENV SENSOR_NAME NULL 
ENV SENSOR_PASS NULL
ENV KEYSTORE_PASS NULL
ENV REDIS_URL redis
RUN ["./gradlew","clean"]
RUN ["./gradlew","bootJar"]
