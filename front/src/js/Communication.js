import Player from "./Player";
import Map from "./Map";
import {Stomp} from "@stomp/stompjs/esm6";

export default class Communication {

    constructor(){

        //Try to connect to the server
        let connect_callback = function() {
            // called back after the client is connected and authenticated to the STOMP server
            clientSocket.subscribe("/broker/mainMap", Communication.initGame);
            clientSocket.subscribe("/broker/move", callback);
            clientSocket.send("/init", {}, null);
        };
        let error_callback = function(error) {
            alert(error.headers.message);
        };



        let clientSocket = Stomp.client("ws://localhost:8080/connect");
        clientSocket.connect(null, null, connect_callback, error_callback);
    }



    //Function call only once to fetch all information about game
    static initGame = function(message) {
        // called when the client receives a STOMP message from the server
        if (message.body) {
            //For each item in the map, we print it (TODO: use switch and print only diff)

            for (let mesh of JSON.parse(message.body)){
                if(mesh.hasOwnProperty("breed")){
                    let player = new Player();
                    player.setUsername(mesh.name);
                    player.setPosition(new BABYLON.Vector3(mesh.position.x, mesh.position.y , mesh.position.z));
                }else Map.createLandPlot(mesh.position.x, mesh.position.y ,mesh.position.z);
            }
        } else {
            console.log("got empty message");
        }
    };

}