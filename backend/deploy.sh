#!/bin/bash

mvn package -DskipTests

scp -r Dockerfile target/kubys-*.jar josquin@kubys.fr:~/backend

#Maybe think about destroy all running containers
ssh josquin@kubys.fr -t "cd ~/backend && 
	docker build -t kubys_backend . && 
	docker stop $(docker ps | grep kubys_backend | cut -d " " -f1) ;
	docker run -d -p 8080:8080 kubys_backend" 
