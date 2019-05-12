# Welcome to Kubys client

## How can I run the server on my computer ?

First clone the repository :
```
            git@github.com:Joalien/Kubys.git
$ git clone
            https://github.com/Joalien/Kubys.git
```
Then go inside the frontend directory
```$ cd front```
Then, run the following command to transpile the code and run a webpack dev server with hot reloading.
```$ npm run dev```


**Congratulation !** You now have a running kubys client on your own computer. You can access it via [localhost:4242](localhost:4242).

The default behaviour is to connect to the production server. It is fine if you want to work on the client side only. 

However, you might be interested by running the client and server  side by side. If so, you can follow the [guide](https://github.com/Joalien/Kubys/tree/master/backend) to run the server on your computer.
**IF YOU DO SO, THINK ABOUT CHANGING THE LOCATION OF THE BACKEND SERVER IN THE CODE** _(current position : src/js/Communication.js)_



