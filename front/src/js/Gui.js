import {AdvancedDynamicTexture, Button, Rectangle, TextBlock} from 'babylonjs-gui';
import Communication from "./Communication";
import Game from "./Game.js";
import firebase from "firebase/app";
import 'firebase/auth';
import {Scene} from "babylonjs";
import FightGui from "./FightGui";
import Camera from "./Camera";
import Map from "./Map";

export default class Gui {

    static playPlayerButton;
    static playerId;
    static logOutButton;
    static skillTreeButton;
    static skillTreePanel;
    static isComponentPanelOpen = false;
    static isSubscribingFight = false;

    constructor(scene) {
        this.advancedTexture = AdvancedDynamicTexture.CreateFullscreenUI("Menu Principal", true, scene);

        Gui.logOutButton = Button.CreateImageOnlyButton("Log out", "resources/images/icon_logout.png");
        this.setDefaultButtonCharacteristics(Gui.logOutButton, 48, -45);
        Gui.logOutButton.onPointerClickObservable.add(() => {
            firebase.auth().signOut();
        });
        this.advancedTexture.addControl(Gui.logOutButton);

        Gui.playPlayerButton = Button.CreateSimpleButton("Play", "Jouer");
        this.setDefaultButtonCharacteristics(Gui.playPlayerButton, 0, -35);
        Gui.playPlayerButton.color = "white";
        Gui.playPlayerButton.width = "80px";
        Gui.playPlayerButton.onPointerClickObservable.add(() => {
            Communication.sendMessage("/setPlayer", Gui.playerId);
            this.advancedTexture.removeControl(Gui.playPlayerButton);
            Game.CURRENT_SCENE.MAP.clearRingSelection();
            this.advancedTexture.addControl(Gui.subscribeFightButton);
            this.advancedTexture.addControl(Gui.skillTreeButton);
        });


        Gui.subscribeFightButton = Button.CreateImageOnlyButton("Fight !", "resources/images/icon_sword.png");
        this.setDefaultButtonCharacteristics(Gui.subscribeFightButton, -45, 25);
        this.surroundWithColor(Gui.subscribeFightButton);
        Gui.subscribeFightButton.onPointerClickObservable.add(() => {
            Gui.isSubscribingFight = !Gui.isSubscribingFight;
            if (Gui.isSubscribingFight) {
                Communication.sendMessage("/fight/subscribe", {});
                Gui.subscription = Communication.clientSocket.subscribe("/broker/fight", message => {
                    // First, let's create the fight map
                    Game.FIGHT_SCENE = new Scene(Game.ENGINE);
                    Game.FIGHT_SCENE.GUI = new FightGui();
                    Game.FIGHT_SCENE.CAMERA = new Camera(Game.FIGHT_SCENE, Game.CANVAS);
                    Game.FIGHT_SCENE.MAP = new Map(Game.FIGHT_SCENE, Game.FIGHT_SCENE.CAMERA);
                    Game.FIGHT_SCENE.MAP.NAME = "Fight map";
                    Game.FIGHT_SCENE.NAME = "Fight scene";

                    // Then, let's update the topic's subscription
                    let fightId = message.body;
                    Gui.subscription.unsubscribe();
                    Communication.unsubscribeAll();

                    Communication.updateSubscription(Game.FIGHT_SCENE);
                    Communication.sendMessage("/getAllMap", null);
                    Communication.sendMessage("/getSpells", null);
                    Communication.clientSocket.subscribe("/broker/fight/" + fightId, payload => console.log("Received from /fight/" + fightId + " : " + payload.body));
                    Communication.sendMessage("/fight/" + fightId + "/move/53", null);
                    Communication.sendMessage("/fight/" + fightId + "/use/52/on/48", null);
                    Communication.sendMessage("/fight/" + fightId + "/endTurn", null);
                    Communication.sendMessage("/fight/" + fightId + "/winGame", null);

                    Game.switchScene(Game.FIGHT_SCENE);
                });
            } else {
                Communication.sendMessage("/fight/unsubscribe", {});
                Gui.subscription.unsubscribe();
            }
        });

        Gui.skillTreeButton = Button.CreateImageOnlyButton("Arbre de compétence", "resources/images/icon_parchemin.png");
        this.setDefaultButtonCharacteristics(Gui.skillTreeButton, -45, 40);
        this.surroundWithColor(Gui.skillTreeButton);
        Gui.skillTreeButton.onPointerClickObservable.add(() => {
            if (Gui.isComponentPanelOpen)
                this.advancedTexture.removeControl(Gui.skillTreePanel);
            else {
                Communication.mockRestApi("/user/getSpells", message => this.createComponentTreePanel(message));
                Communication.sendMessage("/getSpells", null);
            }
            Gui.isComponentPanelOpen = !Gui.isComponentPanelOpen;
        });
    }

    addPlayButton(playerId) {
        Gui.playerId = playerId;
        this.advancedTexture.addControl(Gui.playPlayerButton);
    };

    removePlayButton(playerId) {
        this.advancedTexture.removeControl(Gui.playPlayerButton);
    };

    setDefaultButtonCharacteristics(button, left, top) {
        button.isClicked = false;
        button.width = "50px";
        button.height = "50px";
        button.cornerRadius = 40;
        button.alpha = 0.9;
        button.color = "grey";
        button.top = top + "%";
        button.left = left + "%";
    }

    surroundWithColor(button) {
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
                this.setDefaultButtonCharacteristics(axe, positionValues[i].width, positionValues[i++].height);
                axe.alpha = 0.6;
                Gui.skillTreePanel.addControl(axe);
                axe.onPointerEnterObservable.add(() => {
                    axe.alpha = 2;
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
                axe.onPointerOutObservable.add(() => axe.alpha = 0.6);
            }
        } else {
            console.log("got empty message");
        }
    }
};
