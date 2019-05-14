#!/bin/bash

npm run build 
cp -R textures/ dist/

scp -r Dockerfile dist/ josquin@192.168.1.2:~/front

#Maybe think about destroy all running containers
ssh josquin@192.168.1.2 -t "cd ~/front && 
	docker build -t kubys_frontend . && 
	docker stop $(docker ps | grep kubys_frontend | cut -d " " -f1) ;
	docker run -d -p 80:80 -p 443:443 -v /home/josquin/front/certs/:/etc/ssl  kubys_frontend"
