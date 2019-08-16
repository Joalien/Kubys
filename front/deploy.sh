#!/bin/bash

npm run build 
rsync -avzc resources dist/

rsync -avzc Dockerfile reverse_proxy_nginx.conf dist josquin@kubys.fr:~/front

