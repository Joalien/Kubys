import Game from "./Game";
import {Stomp} from "@stomp/stompjs/esm6";
import firebase from "firebase/app";
import 'firebase/auth';
import Player from "./Player";
import Gui from "./Gui";

export default class Communication {

    static getAllMapSubscription;
    static clientSocket;
    static updateMapSubscription;
    static getPlayerSubscription;
    static getSpellSubscription;

    constructor() {
        let url;
        switch(process.env.NODE_ENV) {
            case "production":
                url = "wss://kubys.fr:8443/connect"; // prod
                break;
            case "development":
                url = "wss://localhost:8443/connect"; // docker-compose in dev
                break;
            case "none":
                url = "ws://localhost:8080/connect"; // only dev
                break;
        }

        Communication.clientSocket = Stomp.client(url);

        //Try to connect to the server
        let connect_callback = () => {
            console.log("Connected with server !");
            // called back after the client is connected and authenticated to the STOMP server
            Communication.updateSubscription(Game.CURRENT_SCENE);
            Communication.clientSocket.subscribe("/user/errors", error => console.log(error));
            Communication.clientSocket.subscribe("/user/setPlayer", message => Player.refreshPlayer(message.body));

            // Communication.sendMessage("/getAllMap", null);
            Communication.clientSocket.send("/getPlayers", null);
            // Communication.sendMessage("/command", JSON.stringify("CREATE"));
            window.addEventListener("keypress", Communication.pressEvent);
        };

        firebase.auth().onAuthStateChanged(function (user) {
            if (user) { // User is signed in.
                user.getIdToken(true).then((token) => {
                    Communication.clientSocket.connect(token, null, connect_callback, (error) => {
                        console.error(error);
                        Communication.redirectUser();
                    });
                });
            } else {
                Communication.redirectUser();
            }
        });

    }

    static redirectUser() {
        console.log("Not connected : ");
        document.location.href = "/login.html";
    }


    static sendMessage(endpoint, message) {
        message = message===0?"0":message;
        Communication.clientSocket.send(endpoint, {}, message);
    }


    static pressEvent = function(evt) {
        switch (evt.key) {
            case 'z':
            case 's':
            case 'q':
            case 'd':
                Communication.sendMessage("/command", JSON.stringify(evt.key));
        }
    }

    static updateSubscription(scene) {
        Communication.getAllMapSubscription = Communication.clientSocket.subscribe("/user/getAllMap", scene.MAP.getAllMap);
        Communication.getPlayerSubscription = Communication.clientSocket.subscribe("/user/getPlayers", scene.MAP.selectionRing);
        Communication.getSpellSubscription = Communication.clientSocket.subscribe("/user/getSpells", message => scene.GUI.createComponentTreePanel(message));
        Communication.updateMapSubscription = Communication.clientSocket.subscribe("/broker/command", scene.MAP.updateMap);
    }

    static unsubscribeAll() {
        [Communication.getAllMapSubscription, Communication.updateMapSubscription, Communication.getPlayerSubscription, Communication.getSpellSubscription].map(s => s.unsubscribe());
    }
}

