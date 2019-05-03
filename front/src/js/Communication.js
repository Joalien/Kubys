import Player from "./Player";
import Map from "./Map";
import {Stomp} from "@stomp/stompjs/esm6";

export default class Communication {

    static getAllMapSubscription;
    static clientSocket;
    constructor(){


        
        // fetch(`http://localhost:8080/login`, {
        //     method: "POST",
        //     body: JSON.stringify({username: "user", passcode: "63ea0042-efe7-4413-8e74-7d9d6b33af2c"})
        // }).then((response) => console.log(response)).catch(((response) => console.log(response)));


        Communication.clientSocket = Stomp.client("ws://127.0.0.1:8080/connect");

        //Try to connect to the server
        let connect_callback = function() {
            // called back after the client is connected and authenticated to the STOMP server
            Communication.getAllMapSubscription = Communication.clientSocket.subscribe("/user/getAllMap", Communication.getAllMap);
            Communication.getAllMapSubscription = Communication.clientSocket.subscribe("/user/errors", (error) => console.log(error));
            Communication.clientSocket.subscribe("/broker/move", () => {});
            Communication.clientSocket.send("/getAllMap", null);

        };

        Communication.clientSocket.connect({login: "login"}, {passcode: "passcode"}, connect_callback, ()=> console.log("error trying to connect to the server"));
    }



    //Function call only once to fetch all information about game
    static getAllMap = function(message) {
        // called when the client receives a STOMP message from the server
        if (message.body) {
            //For each item in the map, we print it

            for (let mesh of JSON.parse(message.body)){
                if(mesh.hasOwnProperty("breed")){
                    let player = new Player();
                    player.setUsername(mesh.name);
                    player.setPosition(new BABYLON.Vector3(mesh.position.x, mesh.position.y , mesh.position.z));
                }else Map.createLandPlot(mesh.position.x, mesh.position.y ,mesh.position.z);
            }

            Communication.getAllMapSubscription.unsubscribe();
        } else {
            console.log("got empty message");
        }
    };

}