#!/bin/bash

bash -c "cd backend && ./deploy.sh" 
bash -c "cd front && ./deploy.sh"
bash -c "cd nginx && ./deploy.sh"

scp docker-compose.yml josquin@kubys.fr:~/kubys

ssh josquin@kubys.fr -t "export LC_ALL=C.UTF-8;
			 export LANG=C.UTF-8;
			 cd ~/kubys;
			 pipenv run docker-compose up --build -d"
