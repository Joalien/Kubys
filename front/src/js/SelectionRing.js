import "babylonjs-loaders";
import Player from "./Player";
import MainMap from "./MainMap";
import Communication from "./Communication"
import {AdvancedDynamicTexture, Button} from 'babylonjs-gui';
import Camera from "./Camera";
import Game from "./Game";
import MapUtilities from "./MapUtilities";
import Gui from "./Gui";
import firebase from "firebase";


export default class SelectionRing {

    static ringPlayers = [];

    constructor() {
        this.scene = new BABYLON.Scene(Game.ENGINE);
        this.camera = new Camera(this.scene, Game.CANVAS);
        Game.current_scene = Game.SCENES.push(this.scene) - 1;

        // Create GUI
        this.advancedTexture = AdvancedDynamicTexture.CreateFullscreenUI("Selection ring", true, this.scene);
        this.playPlayerButton = Button.CreateSimpleButton("Play", "Jouer");
        this.playPlayerButton.isClicked = false;
        this.playPlayerButton.height = "50px";
        this.playPlayerButton.cornerRadius = 40;
        this.playPlayerButton.alpha = 0.9;
        this.playPlayerButton.top = "-35%";
        this.playPlayerButton.left = "0%";
        this.playPlayerButton.color = "white";
        this.playPlayerButton.width = "80px";
        this.playPlayerButton.onPointerClickObservable.add(() => {
            this.confirmPickedPlayer(this.playerId);
        });

        let logOutButton = Button.CreateImageOnlyButton("Log out", "resources/images/icon_logout.png");
        Gui.setDefaultButtonCharacteristics(logOutButton, -45, -40);
        // this.advancedTexture.addControl(Gui.addTooltip(logOutButton, "Déconnexion"));
        logOutButton.onPointerClickObservable.add(() => {
            firebase.auth().signOut();
        });
        this.advancedTexture.addControl(logOutButton);

        MapUtilities.createLight(this.scene);
        MapUtilities.createSkybox(this.scene);

        Communication.mockRestApi("/user/getPlayers", message => this.selectionRing(message));
        Communication.sendMessage("/getPlayers");
    }

    confirmPickedPlayer(playerId) {
        Communication.mockRestApi("/user/setPlayer", message => {
            Player.refreshPlayer(message.body);
            this.scene.dispose();
            this.advancedTexture.dispose();
            new MainMap();
        });
        Communication.sendMessage("/setPlayer", playerId);
    }

    selectionRing = async message => {
        console.log("selection ring");
        if (message.body) {
            let i = 0;
            //For each item in the map, we print i
            for (let player of JSON.parse(message.body)) {
                let alpha = (2 * Math.PI / JSON.parse(message.body).length * i) - Math.PI / 2;
                let distance = 3;

                let objPlayer = await Player.build(player, this.scene);

                objPlayer.mesh.position = new BABYLON.Vector3(Math.cos(alpha) * distance, 0, Math.sin(alpha) * distance);
                objPlayer.mesh.rotate(BABYLON.Axis.Y, Math.PI / 2, BABYLON.Space.WORLD);
                objPlayer.mesh.rotate(BABYLON.Axis.Y, -alpha, BABYLON.Space.WORLD);
                objPlayer.mesh.actionManager = new BABYLON.ActionManager(this.scene);

                objPlayer.mesh.actionManager.registerAction(
                    new BABYLON.ExecuteCodeAction(
                        BABYLON.ActionManager.OnPickTrigger, () => {
                            if (SelectionRing.ringPlayers[this.playerId] === objPlayer.mesh) {
                                this.confirmPickedPlayer(this.playerId);
                                return;
                            }
                            let animationBox = new BABYLON.Animation("translatePlayer",
                                "position",
                                500,
                                BABYLON.Animation.ANIMATIONTYPE_VECTOR3,
                                BABYLON.Animation.ANIMATIONLOOPMODE_CONSTANT);
                            animationBox.setKeys([
                                {
                                    frame: 0,
                                    value: objPlayer.mesh.position
                                }, {
                                    frame: 100,
                                    value: new BABYLON.Vector3.Zero()
                                }
                            ]);
                            objPlayer.mesh.animations = [animationBox];

                            for (let j = 0; j < SelectionRing.ringPlayers.length; j++) {
                                if (SelectionRing.ringPlayers[j].position.equals(new BABYLON.Vector3.Zero())) {
                                    this.scene.beginAnimation(SelectionRing.ringPlayers[j], 100, 0, true);
                                }
                                if (SelectionRing.ringPlayers[j] === objPlayer.mesh) {
                                    this.advancedTexture.addControl(this.playPlayerButton);
                                    this.playerId = j;
                                }
                            }

                            let animationCamera = new BABYLON.Animation("translateCamera", "alpha", 120, BABYLON.Animation.ANIMATIONTYPE_FLOAT, BABYLON.Animation.ANIMATIONLOOPMODE_CONSTANT);
                            animationCamera.setKeys([
                                {
                                    frame: 0,
                                    value: this.camera.arcRotateCamera.alpha
                                }, {
                                    frame: 100,
                                    value: (2 * Math.PI / JSON.parse(message.body).length * this.playerId) - Math.PI / 2
                                }
                            ]);
                            this.camera.arcRotateCamera.animations = [animationCamera];
                            this.scene.beginAnimation(this.camera.arcRotateCamera, 0, 100, false, 1, () =>
                                this.scene.beginAnimation(objPlayer.mesh, 0, 100, true)
                            );
                        }));
                SelectionRing.ringPlayers[i++] = objPlayer.mesh;
            }
        }
    };
};
