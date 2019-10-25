import {AdvancedDynamicTexture, Button, Rectangle} from 'babylonjs-gui';
import Communication from "./Communication";
import Map from "./Map.js";
import firebase from "firebase/app";
import 'firebase/auth';
import FightMap from "./FightMap";

export default class Gui {

    static playPlayerButton;
    static playerId;
    static logOutButton;
    static competenceTreeButton;
    static competenceTreePanel;
    static advancedTexture;

    constructor() {
        Gui.advancedTexture = AdvancedDynamicTexture.CreateFullscreenUI("Menu Principal");

        Gui.logOutButton = Button.CreateImageOnlyButton("Log out", "resources/images/icon_logout.png");
        this.setDefaultButtonCharacteristics(Gui.logOutButton, "-45%", "48%");
        Gui.logOutButton.onPointerClickObservable.add(function() {
            firebase.auth().signOut();
        });
        Gui.advancedTexture.addControl(Gui.logOutButton);

        Gui.playPlayerButton = Button.CreateSimpleButton("Play", "Jouer");
        this.setDefaultButtonCharacteristics(Gui.playPlayerButton, "-35%", "0%");
        Gui.playPlayerButton.width = "80px";
        Gui.playPlayerButton.onPointerClickObservable.add(function() {
            Communication.sendMessage("/setPlayer", Gui.playerId);
            Gui.removePlayButton();
            Map.clearRingSelection();
            Gui.advancedTexture.addControl(Gui.competenceTreeButton);
        });


        Gui.subscribeButton = Button.CreateSimpleButton("Fight", "Rejoindre un combat");
        this.setDefaultButtonCharacteristics(Gui.subscribeButton, "20%", "0%");
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

        Gui.competenceTreeButton = Button.CreateImageOnlyButton("Arbre de compétence", "resources/images/icon_parchemin.png");
        this.setDefaultButtonCharacteristics(Gui.competenceTreeButton, "40%", "-40%");
        Gui.competenceTreeButton.onPointerClickObservable.add(function() {
            Communication.clientSocket.subscribe("/user/getSpells", message => {
                if (message.body) {
                    //For each item in the map, we print it
                    for (let spell of JSON.parse(message.body)) {
                        console.log(spell);
                    }
                } else {
                    console.log("got empty message");
                }
            });
            Communication.sendMessage("/getSpells", null);
            Gui.advancedTexture.addControl(Gui.competenceTreePanel);
        });
        Gui.advancedTexture.addControl(Gui.competenceTreeButton); // TODO: remove me

        // Create Competence tree
        Gui.competenceTreePanel = new Rectangle("Panneau d'arbre de compétence");
        Gui.competenceTreePanel.width = "80%";
        Gui.competenceTreePanel.height = "80%";
        Gui.competenceTreePanel.alpha = 0.8;
        Gui.competenceTreePanel.cornerRadius = 100;
        Gui.competenceTreePanel.background = "white";
        Gui.competenceTreePanel.isPointerBlocker = true;

        this.sword = Button.CreateImageOnlyButton("Sword", "resources/images/icon_sword.png");
        this.setDefaultButtonCharacteristics(this.sword, "30%", "-30%");
        Gui.competenceTreePanel.addControl(this.sword);

        this.axe = Button.CreateImageOnlyButton("Axe", "resources/images/icon_axe.png");
        this.setDefaultButtonCharacteristics(this.axe, "30%", "-10%");
        Gui.competenceTreePanel.addControl(this.axe);

        this.bow = Button.CreateImageOnlyButton("Bow", "resources/images/icon_bow.png");
        this.setDefaultButtonCharacteristics(this.bow, "30%", "10%");
        Gui.competenceTreePanel.addControl(this.bow);

        this.poison = Button.CreateImageOnlyButton("Poison", "resources/images/icon_poison.png");
        Gui.competenceTreePanel.addControl(this.poison);
        this.setDefaultButtonCharacteristics(this.poison, "30%", "30%");

    }

    static addPlayButton = function(playerId){
        Gui.playerId = playerId;
        Gui.advancedTexture.addControl(Gui.playPlayerButton);
    };
    static removePlayButton = function() {
        Gui.advancedTexture.removeControl(Gui.playPlayerButton);
    };

    setDefaultButtonCharacteristics(button, top, left) {
        button.width = "50px";
        button.height = "50px";
        button.cornerRadius = 40;
        button.alpha = 0.8;
        button.color = "white";
        button.top = top;
        button.left = left;
    }
};
