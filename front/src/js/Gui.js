import {AdvancedDynamicTexture, Button, Rectangle, TextBlock} from 'babylonjs-gui';
import Communication from "./Communication";
import Map from "./Map.js";
import firebase from "firebase/app";
import 'firebase/auth';
import FightMap from "./FightMap";

export default class Gui {

    static playPlayerButton;
    static playerId;
    static logOutButton;
    static skillTreeButton;
    static skillTreePanel;
    static advancedTexture;
    static isComponentPanelOpen = false;
    static isSubscribingFight = false;

    constructor() {
        Gui.advancedTexture = AdvancedDynamicTexture.CreateFullscreenUI("Menu Principal");

        Gui.logOutButton = Button.CreateImageOnlyButton("Log out", "resources/images/icon_logout.png");
        Gui.setDefaultButtonCharacteristics(Gui.logOutButton, 48, -45);
        Gui.logOutButton.onPointerClickObservable.add(function() {
            firebase.auth().signOut();
        });
        Gui.advancedTexture.addControl(Gui.logOutButton);

        Gui.playPlayerButton = Button.CreateSimpleButton("Play", "Jouer");
        Gui.setDefaultButtonCharacteristics(Gui.playPlayerButton, 0, -35);
        Gui.playPlayerButton.color = "white";
        Gui.playPlayerButton.width = "80px";
        Gui.playPlayerButton.onPointerClickObservable.add(function() {
            Communication.sendMessage("/setPlayer", Gui.playerId);
            Gui.advancedTexture.removeControl(Gui.playPlayerButton);
            Map.clearRingSelection();
            Gui.advancedTexture.addControl(Gui.subscribeFightButton);
        });


        Gui.subscribeFightButton = Button.CreateImageOnlyButton("Fight !", "resources/images/icon_sword.png");
        Gui.setDefaultButtonCharacteristics(Gui.subscribeFightButton, -45, 25);
        Gui.surroundWithColor(Gui.subscribeFightButton);
        Gui.subscribeFightButton.onPointerClickObservable.add(function() {
            Gui.isSubscribingFight = !Gui.isSubscribingFight;
            if (Gui.isSubscribingFight) {
                Communication.sendMessage("/fight/subscribe", {});
                Gui.subscription = Communication.clientSocket.subscribe("/user/fight", FightMap.startFight);
            } else {
                Communication.sendMessage("/fight/unsubscribe", {});
                Gui.subscription.unsubscribe();
            }
        });
    }

    static addPlayButton(playerId) {
        Gui.playerId = playerId;
        Gui.advancedTexture.addControl(Gui.playPlayerButton);
    };

    static removePlayButton(playerId) {
        Gui.advancedTexture.removeControl(Gui.playPlayerButton);
    };

    static setDefaultButtonCharacteristics(button, left, top) {
        button.isClicked = false;
        button.width = "50px";
        button.height = "50px";
        button.cornerRadius = 40;
        button.alpha = 0.9;
        button.color = "grey";
        button.top = top + "%";
        button.left = left + "%";
    }

    static surroundWithColor(button) {
        button.onPointerClickObservable.add(() => {
            button.isClicked = !button.isClicked;
            if (button.isClicked) {
                button.color = "orange";
                button.thickness = 4;
            } else {
                button.color = "grey";
                button.thickness = 1;
            }
        });
    }

    static createComponentTreePanel(message) {
        Gui.skillTreeButton = Button.CreateImageOnlyButton("Arbre de compétence", "resources/images/icon_parchemin.png");
        Gui.setDefaultButtonCharacteristics(Gui.skillTreeButton, -45, 40);
        Gui.surroundWithColor(Gui.skillTreeButton);
        Gui.skillTreeButton.onPointerClickObservable.add(function() {
            if(Gui.isComponentPanelOpen)
                Gui.advancedTexture.removeControl(Gui.skillTreePanel);
            else Gui.advancedTexture.addControl(Gui.skillTreePanel);
            Gui.isComponentPanelOpen = !Gui.isComponentPanelOpen;
        });
        Gui.advancedTexture.addControl(Gui.skillTreeButton);

        // Create Skill tree
        const height = 80;
        const width = 80;
        const treeWidth = 100 * 0.6;
        const offset =  (100 - treeWidth) / 2;
        Gui.skillTreePanel = new Rectangle("Panneau d'arbre de compétence");
        Gui.skillTreePanel.height = height + "%";
        Gui.skillTreePanel.width = width + "%";
        Gui.skillTreePanel.alpha = 0.8;
        Gui.skillTreePanel.cornerRadius = 100;
        Gui.skillTreePanel.background = "white";
        Gui.skillTreePanel.isPointerBlocker = true;

        let nameOfSpell = new TextBlock();
        nameOfSpell.left = treeWidth / 2 + "%";
        Gui.skillTreePanel.addControl(nameOfSpell);

        let positionValues = [
            {width: -offset, height: 40},
            {width: treeWidth/6 - offset, height: 30},
            {width: -treeWidth/6 - offset, height: 30},
            {width: -treeWidth * 2/6 - offset, height: 20},
            {width: -offset, height: 20},
            {width: treeWidth * 2/6 - offset, height: 20},
            {width: -treeWidth/6 - offset, height: 10},
            {width: treeWidth/6 - offset , height: 10},
            {width: -treeWidth * 2/6 - offset, height: 0},
            {width: -offset, height: 0},
            {width: treeWidth * 2/6 - offset, height: 0},
            {width: -treeWidth/6 - offset, height: -10},
            {width: treeWidth/6 - offset, height: -10},
            {width: -treeWidth * 2/6 - offset, height: -20},
            {width: -offset, height: -20},
            {width: treeWidth * 2/6 - offset, height: -20},
            {width: -offset, height: -40},
        ];


        if (message.body) {
            let i=0;
            for (let spell of JSON.parse(message.body)) {
                if (i > positionValues.length) console.error("More spells than supposed, crash !");
                let axe = Button.CreateImageOnlyButton(spell.name, "resources/images/icon_axe.png");
                Gui.setDefaultButtonCharacteristics(axe, positionValues[i].width, positionValues[i++].height);
                axe.alpha = 0.6;
                Gui.skillTreePanel.addControl(axe);
                axe.onPointerEnterObservable.add(() => axe.alpha = 2);
                axe.onPointerOutObservable.add(() => axe.alpha = 0.6);
                axe.onPointerClickObservable.add(() => {
                    nameOfSpell.text =
                        `${spell.name}
                                
Type de sort : ${spell.type}
Point d'action : ${spell.pa}
Portée : ${spell.minScope} à ${spell.maxScope}
Dégats : ${spell.damage}
Zone : ${spell.zone}
`;
                    if (spell.ammunition === -1)
                        nameOfSpell.text += "Munition : ∞";
                    else nameOfSpell.text += "Munition : " + spell.ammunition;
                });
            }
        } else {
            console.log("got empty message");
        }
    }
};
