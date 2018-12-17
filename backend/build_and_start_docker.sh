#!/bin/bash
#Jesli nie dzala to dodaj 'sudo' przed uruchomieniem skryptu
cd server
./gradlew bootJar && docker-compose up -d --build
