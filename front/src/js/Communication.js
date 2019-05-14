import Map from "./Map";
import {Stomp} from "@stomp/stompjs/esm6";
import FightMap from "./FightMap"

export default class Communication {

    static getAllMapSubscription;
    static clientSocket;


    constructor(username){

        Communication.clientSocket = Stomp.client("ws://localhost:8080/connect");

        //Try to connect to the server
        let connect_callback = function() {
            console.log("Connected with server !");
            // called back after the client is connected and authenticated to the STOMP server
            Communication.getAllMapSubscription = Communication.clientSocket.subscribe("/user/getAllMap", Map.getAllMap);
            Communication.clientSocket.subscribe("/user/getSpells", FightMap.getSpells);
            Communication.clientSocket.subscribe("/user/errors", (error) => console.log(error));
            // Communication.getAllMapSubscription = Communication.clientSocket.subscribe("/user/getPlayers", Map.selectionRing);
            Communication.clientSocket.subscribe("/broker/command", Map.updateMap);

            Communication.sendMessage("/getAllMap", null);
            // Communication.clientSocket.send("/getPlayers", null);
            Communication.sendMessage("/command", JSON.stringify("CREATE"));
            window.addEventListener("keypress", Communication.pressEvent);


            Communication.sendMessage("/getSpells", null);
        };
        Communication.clientSocket.connect(username, "passcode", connect_callback, ()=> console.log("error trying to connect to the server"));
    }


    static sendMessage(endpoint, message){
        Communication.clientSocket.send(endpoint, {}, message);
    }


    static pressEvent = function(evt){
        switch (evt.key) {
            case 'z':
            case 's':
            case 'q':
            case 'd':
                Communication.sendMessage("/command", JSON.stringify(evt.key));
        }
    }


}

