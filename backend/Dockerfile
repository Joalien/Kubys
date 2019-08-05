FROM arm32v7/adoptopenjdk:11.0.5_10-jdk-hotspot-bionic

EXPOSE 8080

# TODO : Does not work if multiple .jar
COPY target/kubys-*.jar .

CMD java -jar kubys-*.jar



