import * as GUI from 'babylonjs-gui';
import * as firebase from "firebase";
import Communication from "./Communication";


export default class Gui {

    static panel;
    static playPlayerButton;
    static playerId;


    constructor() {

        this.advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("Menu Principal");
        Gui.panel = new BABYLON.GUI.StackPanel();
        Gui.panel.verticalAlignment = BABYLON.GUI.Control.VERTICAL_ALIGNMENT_BOTTOM;
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


        let signOut = BABYLON.GUI.Button.CreateSimpleButton("signOut", "Déconnexion");
        signOut.color = "white";
        signOut.height = "40px";
        signOut.onPointerClickObservable.add(function() {
            firebase.auth().signOut();
            firebase.default.auth().onAuthStateChanged(function(user) {
                if (!user) {
                    // User is signed in.
                    document.location.href = "/login.html";
                }
            });
        });
        Gui.panel.addControl(signOut);

        Gui.playPlayerButton = BABYLON.GUI.Button.CreateSimpleButton("playPlayerButton", "Play");
        Gui.playPlayerButton.color = "white";
        Gui.playPlayerButton.height = "40px";
        Gui.playPlayerButton.onPointerClickObservable.add(function() {
            Communication.sendMessage("/setPlayer", Gui.playerId);
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
        console.log("addButton "+playerId);
        Gui.playerId = playerId;
        Gui.panel.addControl(Gui.playPlayerButton);
    };
    static removePlayButton = function(){
        console.log("removeButton");
            Gui.panel.removeControl(Gui.playPlayerButton);
    };
};
