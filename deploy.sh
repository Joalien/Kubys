#!/bin/bash

bash -c "cd backend && ./deploy.sh" 
bash -c "cd front && ./deploy.sh"

ssh josquin@kubys.fr -t "export LC_ALL=C.UTF-8;
			 export LANG=C.UTF-8;
			 pipenv run docker-compose up -d --force-recreate"