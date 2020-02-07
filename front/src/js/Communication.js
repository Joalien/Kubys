import {Stomp} from "@stomp/stompjs/esm6";
import firebase from "firebase/app";
import 'firebase/auth';

export default class Communication {

    static clientSocket;

    constructor(callback) {
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

        firebase.auth().onAuthStateChanged(user => {
            if (user) { // User is signed in.
                user.getIdToken(true).then(token => {
                    Communication.clientSocket.connect(token, null, callback, error => {
                        console.error("Can't establish connection to server : " + error);
                        Communication.redirectUser();
                    });
                });
            } else {
                Communication.redirectUser();
            }
        });

    }

    static redirectUser() {
        document.location.href = "/login.html";
    }


    static sendMessage(endpoint, message) {
        message = message===0?"0":message;
        Communication.clientSocket.send(endpoint, {}, message);
    }

    static mockRestApi = (url, callback) => {
        let subscription = Communication.clientSocket.subscribe(url, message => {
            subscription.unsubscribe();
            callback(message);
        });
    }
}

