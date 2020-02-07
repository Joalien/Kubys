import {AdvancedDynamicTexture, Button, Rectangle, TextBlock} from 'babylonjs-gui';
import Communication from "./Communication";
import Game from "./Game.js";
import firebase from "firebase/app";
import 'firebase/auth';
import {Scene} from "babylonjs";
import FightGui from "./FightGui";
import Camera from "./Camera";
import MainMap from "./MainMap";

export default class Gui {

    static playPlayerButton;
    static playerId;
    static logOutButton;
    static skillTreeButton;
    static skillTreePanel;
    static isComponentPanelOpen = false;
    static isSubscribingFight = false;

    constructor(scene) {
    }

    // addPlayButton(playerId) {
    //     Gui.playerId = playerId;
    //     this.advancedTexture.addControl(Gui.playPlayerButton);
    // };
    //
    // removePlayButton(playerId) {
    //     this.advancedTexture.removeControl(Gui.playPlayerButton);
    // };

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

    createComponentTreePanel(message) {
        console.log('create tree panel');

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
        this.advancedTexture.addControl(Gui.skillTreePanel);

        let spellPoints = new TextBlock();
        spellPoints.left = treeWidth / 2 + "%";
        spellPoints.top = height * (-0.5) + "%";
        Communication.clientSocket.send("/getSpellPoints", {}, null);
        Communication.clientSocket.subscribe("/user/getSpellPoints", message => spellPoints.text = "Vous possèdez " + message.body + " points de sort");
        Gui.skillTreePanel.addControl(spellPoints);

        let nameOfSpell = new TextBlock();
        nameOfSpell.left = treeWidth / 2 + "%";
        Gui.skillTreePanel.addControl(nameOfSpell);

        let positionValues = [
            { width: -offset, height: 40 },
            { width: treeWidth/6 - offset, height: 30 },
            { width: -treeWidth/6 - offset, height: 30 },
            { width: -treeWidth * 2/6 - offset, height: 20 },
            { width: -offset, height: 20 },
            { width: treeWidth * 2/6 - offset, height: 20 },
            { width: -treeWidth/6 - offset, height: 10 },
            { width: treeWidth/6 - offset , height: 10 },
            { width: -treeWidth * 2/6 - offset, height: 0 },
            { width: -offset, height: 0 },
            { width: treeWidth * 2/6 - offset, height: 0 },
            { width: -treeWidth/6 - offset, height: -10 },
            { width: treeWidth/6 - offset, height: -10 },
            { width: -treeWidth * 2/6 - offset, height: -20 },
            { width: -offset, height: -20 },
            { width: treeWidth * 2/6 - offset, height: -20 },
            { width: -offset, height: -40 },
        ];

        if (message.body) {
            let i=0;
            for (let spell of JSON.parse(message.body)) {
                if (i > positionValues.length) console.error("More spells than supposed, crash !");
                let spellButton = Button.CreateImageOnlyButton(spell.name, "resources/images/icon_axe.png");
                this.setDefaultButtonCharacteristics(spellButton, positionValues[i].width, positionValues[i++].height);
                spellButton.alpha = 0.6;
                Gui.skillTreePanel.addControl(spellButton);
                spellButton.onPointerEnterObservable.add(() => {
                    spellButton.alpha = 2;
                    nameOfSpell.text = // TODO uncomment
                        `${spell.name}

Type de sort : ${spell.type/*.label*/}
Point d'action : ${spell.pa}
Portée : ${spell.minScope} à ${spell.maxScope}
Dégats : ${spell.damage}
Zone : ${spell.zone}
`;
                    if (spell.ammunition === -1)
                        nameOfSpell.text += "Munition : ∞";
                    else nameOfSpell.text += "Munition : " + spell.ammunition;

                });
                spellButton.onPointerOutObservable.add(() => spellButton.alpha = 0.6);
                this.surroundWithColor(spellButton);
            }
        } else {
            console.log("got empty message");
        }
    }

    static createLine = (node1, node2) => {
        let line = new BABYLON.GUI.Line();
        line.x1 = node1.width;
        line.y1 = node1.height;
        line.x2 = node2.width;
        line.y2 = node2.height;
        line.lineWidth = 5;
        line.color = "red";
        return line;
    }
};
