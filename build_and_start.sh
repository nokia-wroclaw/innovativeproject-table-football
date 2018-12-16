#!/bin/bash

cd ./backend

./gradlew bootJar

cd ..

sudo docker-compose up -d --build
