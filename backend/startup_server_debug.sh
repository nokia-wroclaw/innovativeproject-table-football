#!/bin/bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000 -jar server*.jar --spring.redis.host=$REDIS_URL --server.ssl.key-store-password=$KEYSTORE_PASS --server.ssl.trust-store-password=$KEYSTORE_PASS --spring.security.user.name=$SENSOR_NAME --spring.security.user.password=$SENSOR_PASS 

KEYSTORE_PASS=""
SENSOR_NAME=""
SENSOR_PASS=""
