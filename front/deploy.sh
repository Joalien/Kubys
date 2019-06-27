#!/bin/bash

npm run build 
rsync -avzc resources dist/

rsync -avzc Dockerfile dist josquin@kubys.fr:~/front

