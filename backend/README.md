# Welcome to Kubys server

## How can I run the server on my computer ?

First clone the repository :
```
            git@github.com:Joalien/Kubys.git
$ git clone
            https://github.com/Joalien/Kubys.git
```
Then go inside the backend directory
```$ cd backend```
Compile the source into a fat jar
```$ mvn clean package -DskipTests```
Run the jar
```$ java -jar target/kubys-*.jar```

**Congratulation !** You now have a running kubys backend on your own computer. You can access it via [localhost:8080](localhost:8080).

As the server expects to receive websocket, you might be interested  by running the [client side](https://github.com/Joalien/Kubys/tree/master/front) too.

