#!/bin/bash

java -jar server*.jar --spring.redis.host=$REDIS_URL --server.ssl.key-store-password=$KEYSTORE_PASS --server.ssl.trust-store-password=$KEYSTORE_PASS --spring.security.user.name=$SENSOR_NAME --spring.security.user.password=$SENSOR_PASS --admin.user.name=$ADMIN_USER --admin.password=$ADMIN_PASS

KEYSTORE_PASS=""
SENSOR_NAME=""
SENSOR_PASS=""
ADMIN_USER=""
ADMIN_PASS=""
