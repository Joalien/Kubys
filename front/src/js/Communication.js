import Player from "./Player";
import Map from "./Map";
import {Stomp} from "@stomp/stompjs/esm6";
import FightMap from "./FightMap"

export default class Communication {

    static getAllMapSubscription;
    static clientSocket;
    static scene;
    static lastObservable;
    constructor(scene, username){

        Communication.scene = scene;

        /*92.169.68.158*/



        Communication.clientSocket = Stomp.client("ws://92.169.68.158:8080/connect");

        //Try to connect to the server
        let connect_callback = function() {
            // called back after the client is connected and authenticated to the STOMP server
            Communication.getAllMapSubscription = Communication.clientSocket.subscribe("/user/getAllMap", Communication.getAllMap);
            Communication.getAllMapSubscription = Communication.clientSocket.subscribe("/user/getSpells", FightMap.getSpells);
            Communication.getAllMapSubscription = Communication.clientSocket.subscribe("/user/errors", (error) => console.log(error));
            // Communication.getAllMapSubscription = Communication.clientSocket.subscribe("/user/getPlayers", Map.selectionRing);
            Communication.clientSocket.subscribe("/broker/command", Communication.updateMap);

            Communication.clientSocket.send("/getAllMap", null);
            // Communication.clientSocket.send("/getPlayers", null);
            Communication.clientSocket.send("/command", {}, JSON.stringify("CREATE"));
            window.addEventListener("keypress", Communication.pressEvent);

            // window.removeEventListener("keypress", Communication.pressEvent, false);
            // window.addEventListener("keypress", Communication.pressEvent, false);


            setTimeout(() => Communication.clientSocket.send("/getSpells", null), 1000);

        };
        Communication.clientSocket.connect(username, "passcode", connect_callback, ()=> console.log("error trying to connect to the server"));
    }



    //Function call only once to fetch all information about game
    static getAllMap = function(message) {
        // called when the client receives a STOMP message from the server
        if (message.body) {
            //For each item in the map, we print it

            for (let player of JSON.parse(message.body)){
                if(player.hasOwnProperty("breed")){//Check if player could be optimized
                    let objPlayer = new Player(Communication.scene, player);
                    objPlayer.setLabel(player.name);
                    objPlayer.setPosition(new BABYLON.Vector3(player.position.x, player.position.y, player.position.z));
                }else
                    Map.createLandPlot(player.position.x, player.position.y ,player.position.z);
            }

            Communication.getAllMapSubscription.unsubscribe();
        } else {
            console.log("got empty message");
        }
    };

    static updateMap = function(message){
        if (message.body) {

            let player = JSON.parse(message.body);

            let mesh = Communication.scene.getMeshByID(player.id);
            console.log(player);

            if (mesh == null) { //If new player
                let objPlayer = new Player(Communication.scene, player);
                objPlayer.setLabel(player.name);
                objPlayer.setPosition(new BABYLON.Vector3(player.position.x, player.position.y, player.position.z));
            } else if(player.connected===false){// If player disconnect
                Player.NAME_LABEL[mesh].dispose();
                mesh.dispose();
            } else {//If player had already been created and should move (normal case)

                Player.PLAYERS[player.id] = player;
                let newPosition = new BABYLON.Vector3(player.position.x, player.position.y, player.position.z);

                let animationBox = new BABYLON.Animation("translatePlayer", "position", 500, BABYLON.Animation.ANIMATIONTYPE_VECTOR3, BABYLON.Animation.ANIMATIONLOOPMODE_CONSTANT);
                let keys = [];

                keys.push({
                    frame: 0,
                    value: mesh.position
                });

                keys.push({
                    frame: 100,
                    value: newPosition
                });
                animationBox.setKeys(keys);
                mesh.animations = [];
                mesh.animations.push(animationBox);
                Communication.scene.beginAnimation(mesh, 0, 100, true);

                mesh.position = newPosition;
            }
        } else {
            console.log("got empty message, maybe player can't move");
        }
    };

    static pressEvent = function(evt){
        switch (evt.key) {
            case 'z':
            case 's':
            case 'q':
            case 'd':
                Communication.clientSocket.send("/command", {}, JSON.stringify(evt.key));
        }
    }


}

