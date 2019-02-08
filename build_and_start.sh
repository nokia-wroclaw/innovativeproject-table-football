#!/bin/bash

mkdir -p backend/log 

cd ./backend

./gradlew bootJar

cd ..

sudo docker-compose up --build
