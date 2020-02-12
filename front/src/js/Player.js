import {Rectangle, AdvancedDynamicTexture, TextBlock} from 'babylonjs-gui'
import 'babylonjs-loaders'
import Game from "./Game";
import Communication from "./Communication";

export default class Player {

    static NAME_LABEL = {};
    static CURRENT_PLAYER_ID;

    static elf;
    static dwarf;
    static wizard;
    static berserker;

    static init(scene) {
        return new Promise(resolve => {
            if (!Player.wizard || Player.wizard._scene !== scene) {
                BABYLON.SceneLoader.LoadAssetContainer("/resources/objects/wizard/", "wizard.obj", scene, (container) => {
                    Player.wizard = BABYLON.Mesh.MergeMeshes(container.meshes, true, true, undefined, false, true);
                    Player.wizard.visibility = 0;

                    let scaleFactor = Player.getScalingVectorToFit(Player.wizard);
                    scaleFactor = Math.min(scaleFactor.x, scaleFactor.y, scaleFactor.z);
                    Player.wizard.scaling = new BABYLON.Vector3(scaleFactor, scaleFactor, scaleFactor);
                    Player.wizard._scene = scene;
                    // TODO Improve me !
                    resolve();
                });
            } else {
                resolve();
            }
        });
    }

    static async build(player, scene) {
        await Player.init(scene);

        let myObj = new Player();
        myObj.advancedTexture = AdvancedDynamicTexture.CreateFullscreenUI("Player's name", true, scene);

        if (player.characteristics.some(characteristics => characteristics.name === "DWARF")) {
            // myObj.mesh = Player.dwarf.clone();
            myObj.mesh = Player.wizard.clone();
        } else console.error("Unknown breed !");


        myObj.mesh.id = player.id;
        myObj.mesh.visibility = 1;


        let label = new TextBlock();
        label.text = player.name;

        let rect1 = new Rectangle("rect" + player.id);
        rect1.id = "rect" + player.id;
        rect1.width = (player.name.length + 1) * 10 + "px";
        rect1.height = "40px";
        rect1.cornerRadius = 20;
        rect1.color = "blue";
        rect1.thickness = 3;
        rect1.addControl(label);
        rect1.linkOffsetY = -70;

        myObj.advancedTexture.addControl(rect1);
        rect1.linkWithMesh(myObj.mesh);

        Player.NAME_LABEL["rect" + player.id] = rect1;
        return myObj;
    }

    setPosition(position) {
        this.mesh.position = position;
    }


    static getScalingVectorToFit(mesh) {
        let otherVector = BABYLON.Vector3.One();
        if (!mesh.__scaleVectorCache) {
            mesh.__scaleVectorCache = BABYLON.Vector3.Zero();
        }

        let size = Player.getAbsoluteSize(mesh);
        mesh.__scaleVectorCache.x = otherVector.x / size.x;
        mesh.__scaleVectorCache.y = otherVector.y / size.y;
        mesh.__scaleVectorCache.z = otherVector.z / size.z;

        return mesh.__scaleVectorCache;
    };


    static getAbsoluteSize(mesh) {
        if (!mesh.__size) {
            mesh.__size = BABYLON.Vector3.Zero();
        }

        let bounds = mesh.getBoundingInfo();
        mesh.__size.x = Math.abs(bounds.minimum.x - bounds.maximum.x);
        mesh.__size.y = Math.abs(bounds.minimum.y - bounds.maximum.y);
        mesh.__size.z = Math.abs(bounds.minimum.z - bounds.maximum.z);

        return mesh.__size;
    };

    static refreshPlayer(playerId) {
        Player.CURRENT_PLAYER_ID = Number(playerId);
    }
};