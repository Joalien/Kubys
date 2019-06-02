#!/bin/bash

npm run build 
rsync -avzc textures resources dist/

rsync -avzc Dockerfile dist josquin@kubys.fr:~/front

#Maybe think about destroy all running containers
	ssh josquin@kubys.fr -t 'cd ~/front && 
	docker stop $(docker ps | grep kubys_frontend | cut -d " " -f1) ;
	docker build -t kubys_frontend . && 
	docker run -d -p 80:80 -p 443:443 -v /home/josquin/front/certs/:/etc/ssl -v /home/josquin/front/dist/:/usr/share/nginx/html kubys_frontend'
