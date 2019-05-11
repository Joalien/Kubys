#FROM maven:3.6.0-jdk-11-slim as build
FROM openjdk


#COPY src /
#COPY pom.xml /

WORKDIR /

#RUN mvn -DskipTests clean package 
#spring-boot:repackage 
#RUN unzip /target/kubys-1.0.0.jar
#RUN cat /META-INF/MANIFEST.MF

COPY target/kubys-1.0.0.jar target/kubys-1.0.0.jar



#FROM openjdk:11-jdk-slim

#COPY --from=build /target/kubys-1.0.0.jar /kubys-1.0.0.jar
EXPOSE 8080 

#Only for test purpose
#RUN java -jar /target/kubys-1.0.0.jar


CMD java -jar /target/kubys-1.0.0.jar

