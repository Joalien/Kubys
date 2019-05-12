# Welcome to Kubys project

I am happy to see you here !

If all you want is playing, just click [here](http://92.169.68.158:4242/) and you will be redirect to the game.

If you want to help the develop team, do not be shy and do not hesitate to ask questions, we are looking for little hands as well as experimented developers, 3D designers and game designers.

## I want to help, what can I do ?

First, you can send me a message and ask what you can do or what you want to do.
Secondly, you can travel around the code and search for smells or improvement.
Thirdly, you can make pull requests in order to share your improvements with the community.

## Where could I find the code ?

For the **backend**, just go [here](https://github.com/Joalien/Kubys/tree/master/backend)
For the **frontend**, just go [here](https://github.com/Joalien/Kubys/tree/master/front)

## Are you looking for people who can optimize the integration pipeline ?
**Yes**

## I do not know how/want to write code, can I help anyway ?

**Yes**, we have a lot of work to do in order to find spells and weapons name, balance the different breed or to give us ideas about how to plant trees.

## How does the architecture look like ?

Each client communicates with single server (no load balancer yet). 
Client are  embedded inside the browser, no need to install anything.

```mermaid
graph LR
A1(client)
A2(client)
A3(client)
B(server)

A1 -- 8080 --> B
A2 -- 8080 --> B
A3 -- 8080 --> B
```

