FROM openjdk:10-jre-slim
COPY ./build/libs /usr/src/server
COPY startup_server.sh /usr/src/server
WORKDIR /usr/src/server
EXPOSE 8443
EXPOSE 8080
ENV SENSOR_NAME NULL 
ENV SENSOR_PASS NULL
ENV KEYSTORE_PASS NULL
ENV REDIS_URL localhost
