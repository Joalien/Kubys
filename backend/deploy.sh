#!/bin/bash

mvn package -DskipTests && 

scp -r target/kubys-*.jar josquin@kubys.fr:~/backend/target
scp Dockerfile josquin@kubys.fr:~/backend

