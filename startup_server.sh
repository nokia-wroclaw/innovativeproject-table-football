#!/bin/bash

java -jar server*.jar --server.ssl.key-store-password=$KEYSTORE_PASS --server.ssl.trust-store-password=$KEYSTORE_PASS --spring.security.user.name=$SENSOR_NAME --spring.security.user.password=$SENSOR_PASS

KEYSTORE_PASS=""
SENSOR_NAME=""
SENSOR_PASS=""
