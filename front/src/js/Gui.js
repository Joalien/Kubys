import * as GUI from 'babylonjs-gui';
import Communication from "./Communication";
import Map from "./Map.js";
import * as firebase from "firebase";


export default class Gui {

    static panel;
    static playPlayerButton;
    static playerId;
    static logOutButton;


    constructor() {

        this.advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("Menu Principal");
        Gui.panel = new BABYLON.GUI.StackPanel();
        Gui.panel.verticalAlignment = BABYLON.GUI.Control.VERTICAL_ALIGNMENT_TOP;
        this.advancedTexture.addControl(Gui.panel);


        let connectionTextBlock = new BABYLON.GUI.TextBlock();
        connectionTextBlock.color = "white";
        connectionTextBlock.height = "40px";
        firebase.auth().onAuthStateChanged(function(user) {
            if (user) {
                // User is signed in.
                connectionTextBlock.text = user.email + " " +user.displayName;
            }
        });
        Gui.panel.addControl(connectionTextBlock);


        Gui.logOutButton = BABYLON.GUI.Button.CreateSimpleButton("Gui.logOutButton", "Deconnexion");
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

        Gui.playPlayerButton = BABYLON.GUI.Button.CreateSimpleButton("playPlayerButton", "Play");
        Gui.playPlayerButton.color = "white";
        Gui.playPlayerButton.height = "40px";
        Gui.playPlayerButton.onPointerClickObservable.add(function() {
            Communication.sendMessage("/setPlayer", Gui.playerId);
            Gui.removePlayButton();
            Map.clearRingSelection();
        });
        // Gui.panel.addControl(Gui.playPlayerButton);



        // let grid = new BABYLON.GUI.Grid();
        // this.advancedTexture.addControl(grid);
        // grid.background = "black";
        // grid.height = "500px";
        // grid.width = "500px";
        //
        //
        // grid.addColumnDefinition(0.5);
        // grid.addColumnDefinition(0.5);
        // grid.addRowDefinition(0.25);
        // grid.addRowDefinition(0.25);
        // grid.addRowDefinition(0.25);
        // grid.addRowDefinition(0.25);
        //
        //
        // //Connection
        // let connectionTextBlock = new BABYLON.GUI.TextBlock();
        // connectionTextBlock.text = "Connection";
        // connectionTextBlock.color = "white";
        // connectionTextBlock.fontSize = 24;
        // connectionTextBlock.width = "100%";
        // connectionTextBlock.height = "100%";
        // grid.addControl(connectionTextBlock, 0, 0);
        //
        // //Connection email
        // let connectionEmail = new BABYLON.GUI.InputText();
        // connectionEmail.color = "white";
        // connectionEmail.background = null;
        // grid.addControl(connectionEmail, 1, 0);

    }

    static addPlayButton = function(playerId){
        // console.log("playerId : "+playerId);
        Gui.playerId = playerId;
        Gui.panel.addControl(Gui.playPlayerButton);
    };
    static removePlayButton = function(){
        Gui.panel.removeControl(Gui.playPlayerButton);
    };
};
