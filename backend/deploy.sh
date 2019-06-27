#!/bin/bash

mvn package -DskipTests && 

scp -r Dockerfile target/kubys-*.jar josquin@kubys.fr:~/backend

