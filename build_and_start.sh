#!/bin/bash

mkdir -p backend/log 

cd ./backend

./gradlew clean bootJar

cd ..

sudo docker-compose up -d --build
