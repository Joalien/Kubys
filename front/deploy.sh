#!/bin/bash

npm run build 
rsync -avzc textures resources dist/

rsync -avzc Dockerfile dist josquin@kubys.fr:~/front

