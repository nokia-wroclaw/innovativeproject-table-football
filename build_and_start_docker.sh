#!/bin/bash
#Jesli nie dzala to dodaj 'sudo' przed uruchomieniem skryptu
cd server
./gradlew bootJar

cd ..
docker-compose up -d --build
