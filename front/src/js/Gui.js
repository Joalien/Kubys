import * as GUI from 'babylonjs-gui';
import * as firebase from "firebase";


export default class Gui {

    constructor() {

        this.advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("Menu Principal");
        let panel = new BABYLON.GUI.StackPanel();
        panel.verticalAlignment = BABYLON.GUI.Control.VERTICAL_ALIGNMENT_BOTTOM;
        this.advancedTexture.addControl(panel);



        let connectionTextBlock = new BABYLON.GUI.TextBlock();
        connectionTextBlock.color = "white";
        connectionTextBlock.height = "40px";
        firebase.auth().onAuthStateChanged(function(user) {
            if (user) {
                // User is signed in.
                connectionTextBlock.text = user.email + " " +user.displayName;
            }
        });
        panel.addControl(connectionTextBlock);


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
        panel.addControl(signOut);

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
        //
        // //Connection email
        // let connectionPassword = new BABYLON.GUI.InputPassword();
        // connectionPassword.color = "white";
        // connectionPassword.background = null;
        // grid.addControl(connectionPassword, 2, 0);
        //
        //
        // //Inscription
        // let inscriptionTextBlock = new BABYLON.GUI.TextBlock();
        // inscriptionTextBlock.text = "Inscription";
        // inscriptionTextBlock.color = "white";
        // inscriptionTextBlock.fontSize = 24;
        // grid.addControl(inscriptionTextBlock, 0, 1);
        //
        // //Inscription email
        // let inscriptionEmail = new BABYLON.GUI.InputText();
        // inscriptionEmail.color = "white";
        // inscriptionEmail.background = null;
        // grid.addControl(inscriptionEmail, 1, 1);
        //
        // //Inscription email
        // let inscriptionPassword = new BABYLON.GUI.InputPassword();
        // inscriptionPassword.color = "white";
        // inscriptionPassword.background = null;
        // grid.addControl(inscriptionPassword, 2, 1);
        //
        //
        //
        // //Let's connect to your account
        // let playButton = BABYLON.GUI.Button.CreateSimpleButton("button", "Play");
        // playButton.color = "white";
        // playButton.onPointerClickObservable.add(function() {
        //     self.advancedTexture.removeControl(grid);
        //
        //     new Communication(Map.SCENE, input.text);
        //
        //
        // });
        // grid.addControl(playButton, 3, 0);
        // grid.addControl(playButton, 3, 1);

        // //Button to switch between cameras
        // let switchCamera = BABYLON.GUI.Button.CreateSimpleButton("buton", "Switch camera");
        // switchCamera.width = 1;
        // switchCamera.height = "40px";
        // switchCamera.color = "white";
        // switchCamera.onPointerClickObservable.add(function() {
        //     if(camera.scene.activeCamera instanceof BABYLON.UniversalCamera) camera.setArcRotateCamera();
        //     else if(camera.scene.activeCamera instanceof BABYLON.ArcRotateCamera) camera.setUniversalCamera();
        // });
        // this.panel.addControl(switchCamera);


    }
};
