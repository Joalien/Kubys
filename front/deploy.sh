#!/bin/bash

npm run build 
cp -R textures/ dist/

scp Dockerfile dist/ josquin@192.168.1.2:~/front

#Maybe think about destroy all running containers
ssh josquin@192.168.1.2 -t "cd ~/front && docker build -t kubys_frontend . && docker run -d -p 80:80 kubys_frontend"
