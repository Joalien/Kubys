#!/bin/bash

mvn package -DskipTests

scp Dockerfile target/kubys-*.jar josquin@192.168.1.2:~/backend

#Maybe think about destroy all running containers
ssh josquin@192.168.1.2 -t "cd ~/backend && docker build -t kubys_backend . && docker run -d -p 8080:8080 kubys_backend" 
