#!/bin/bash

npm run build 
cp -R textures/ dist/

scp -r Dockerfile dist/ josquin@kubys.fr:~/front

#Maybe think about destroy all running containers
	ssh josquin@kubys.fr -t "cd ~/front && 
	docker build -t kubys_frontend . && 
	docker stop $(docker ps | grep kubys_frontend | cut -d " " -f1) ;
	docker run -d -p 80:80 -p 443:443 -v /home/josquin/front/certs/:/etc/ssl  kubys_frontend"
