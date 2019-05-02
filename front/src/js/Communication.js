import Player from "./Player";
import Map from "./Map";
import {Stomp} from "@stomp/stompjs/esm6";

export default class Communication {

    static getAllMapSubscription;
    constructor(){

        //Try to connect to the server
        let connect_callback = function() {
            // called back after the client is connected and authenticated to the STOMP server
            Communication.getAllMapSubscription = clientSocket.subscribe("/broker/getAllMap", Communication.getAllMap);
            clientSocket.subscribe("/broker/move", () => {});
            clientSocket.send("/getAllMap", {}, null);
        };
        let error_callback = function(error) {
            alert(error.headers.message);
        };



        let clientSocket = Stomp.client("ws://127.0.0.1:8080/connect");
        clientSocket.connect("Joalien", connect_callback, error_callback);
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