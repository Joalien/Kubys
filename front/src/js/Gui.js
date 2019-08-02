import { AdvancedDynamicTexture, StackPanel, Control, TextBlock, Button } from 'babylonjs-gui';
import Communication from "./Communication";
import Map from "./Map.js";
import firebase from "firebase/app";
import 'firebase/auth';
import FightMap from "./FightMap";


export default class Gui {

    static panel;
    static playPlayerButton;
    static playerId;
    static logOutButton;


    constructor() {

        this.advancedTexture = AdvancedDynamicTexture.CreateFullscreenUI("Menu Principal");
        Gui.panel = new StackPanel();
        Gui.panel.verticalAlignment = Control.VERTICAL_ALIGNMENT_TOP;
        this.advancedTexture.addControl(Gui.panel);


        let connectionTextBlock = new TextBlock();
        connectionTextBlock.color = "white";
        connectionTextBlock.height = "40px";
        firebase.auth().onAuthStateChanged(function(user) {
            if (user) {
                // User is signed in.
                connectionTextBlock.text = user.email + " " +user.displayName;
            }
        });
        Gui.panel.addControl(connectionTextBlock);


        Gui.logOutButton = Button.CreateSimpleButton("Gui.logOutButton", "Deconnexion");
        Gui.logOutButton.color = "white";
        Gui.logOutButton.height = "40px";
        Gui.logOutButton.onPointerClickObservable.add(function() {
            firebase.auth().signOut();
            // firebase.default.auth().onAuthStateChanged(function(user) {
            //     if (user) { // User is signed in.
            //         Gui.logOutButton.children[0].text = "Deconnexion";
            //         user.getIdToken().then((token) => {
            //             Communication.clientSocket.connect(token, null, connect_callback, (error) => {
            //                 Communication.redirectUser();
            //             });
            //         });
            //     } else Gui.logOutButton.children[0].text = "Se connecter";
            // });
        });
        Gui.panel.addControl(Gui.logOutButton);

        Gui.playPlayerButton = Button.CreateSimpleButton("playPlayerButton", "Jouer");
        Gui.playPlayerButton.color = "white";
        Gui.playPlayerButton.height = "40px";
        Gui.playPlayerButton.onPointerClickObservable.add(function() {
            Communication.sendMessage("/setPlayer", Gui.playerId);
            Gui.removePlayButton();
            Map.clearRingSelection();
        });
        // Gui.panel.addControl(Gui.playPlayerButton);

        Gui.subscribeButton = Button.CreateSimpleButton("subscribeButton", "Rejoindre un combat");
        Gui.subscribeButton.color = "white";
        Gui.subscribeButton.height = "40px";
        Gui.subscribeButton.onPointerClickObservable.add(function() {
            Gui.isSubscribe = !Gui.isSubscribe;
            if (Gui.isSubscribe) {
                Communication.sendMessage("/fight/subscribe", {});
                Gui.subscribeButton.textBlock.text = "Quitter la file d'attente";
                Gui.subscription = Communication.clientSocket.subscribe("/user/fight", FightMap.startFight);
            } else {
                Communication.sendMessage("/fight/unsubscribe", {});
                Gui.subscribeButton.textBlock.text = "Rejoindre un combat";
                Gui.subscription.unsubscribe();
            }
        });

    }

    static addPlayButton = function(playerId){
        // console.log("playerId : "+playerId);
        Gui.playerId = playerId;
        Gui.panel.addControl(Gui.playPlayerButton);
    };
    static removePlayButton = function() {
        Gui.panel.removeControl(Gui.playPlayerButton);
    };
};
