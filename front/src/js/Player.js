import {Rectangle, AdvancedDynamicTexture, TextBlock} from 'babylonjs-gui'
import 'babylonjs-loaders'
import Game from "./Game";
import Communication from "./Communication";

export default class Player {

    static NAME_LABEL = {};
    static CURRENT_PLAYER_ID;

    static elf;
    static dwarf;
    static assassin;
    static wizard;
    static berserker;

    static init() {
        return new Promise(resolve => {
            if (!Player.wizard || Player.wizard._scene !== Game.CURRENT_SCENE) {
                BABYLON.SceneLoader.LoadAssetContainer("/resources/objects/wizard/", "wizard.obj", Game.CURRENT_SCENE, (container) => {
                    Player.wizard = BABYLON.Mesh.MergeMeshes(container.meshes, true, true, undefined, false, true);
                    Player.wizard.visibility = 0;

                    let scaleFactor = Player.getScalingVectorToFit(Player.wizard);
                    scaleFactor = Math.min(scaleFactor.x, scaleFactor.y, scaleFactor.z);
                    Player.wizard.scaling = new BABYLON.Vector3(scaleFactor, scaleFactor, scaleFactor);
                    // TODO Improve me !
                    resolve();
                });
            } else {
                resolve();
            }
        });
    }

    static async build(characteristics) {
        await Player.init();

        let myObj = new Player();
        myObj.advancedTexture = AdvancedDynamicTexture.CreateFullscreenUI("Player's name", true, Game.CURRENT_SCENE);

        switch (characteristics.breed) {
            case "DWARF":
                // myObj.mesh = Player.dwarf.clone();
                myObj.mesh = Player.wizard.clone();
                break;
            case "ELF":
                // myObj.mesh = Player.elf.clone();
                myObj.mesh = Player.wizard.clone();
                break;
            case "ASSASSIN":
                // myObj.mesh = Player.assassin.clone();
                myObj.mesh = Player.wizard.clone();
                break;
            case "WIZARD":
                myObj.mesh = Player.wizard.clone();
                break;
            case "BERSERKER":
                // myObj.mesh = Player.berserker.clone();
                myObj.mesh = Player.wizard.clone();
                break;
            default :
                console.error("Breed not found !");
                break;
        }
        myObj.mesh.id = characteristics.id;
        myObj.mesh.visibility = 1;


        let label = new TextBlock();
        label.text = characteristics.name;

        let rect1 = new Rectangle("rect" + characteristics.id);
        rect1.id = "rect" + characteristics.id;
        rect1.width = (characteristics.name.length + 1) * 10 + "px";
        rect1.height = "40px";
        rect1.cornerRadius = 20;
        rect1.color = "blue";
        rect1.thickness = 3;
        rect1.addControl(label);
        rect1.linkOffsetY = -70;

        myObj.advancedTexture.addControl(rect1);
        rect1.linkWithMesh(myObj.mesh);

        Player.NAME_LABEL["rect" + characteristics.id] = rect1;
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
        console.log(playerId);
        Player.CURRENT_PLAYER_ID = Number(playerId);
        Communication.sendMessage("/getAllMap", null);
    }

    // //Local Axes
    // static localAxes(size) {
    //     let pilot_local_axisX = BABYLON.Mesh.CreateLines("pilot_local_axisX", [
    //         new BABYLON.Vector3.Zero(), new BABYLON.Vector3(size, 0, 0), new BABYLON.Vector3(size * 0.95, 0.05 * size, 0),
    //         new BABYLON.Vector3(size, 0, 0), new BABYLON.Vector3(size * 0.95, -0.05 * size, 0)
    //     ], Game.CURRENT_SCENE);
    //     pilot_local_axisX.color = new BABYLON.Color3(1, 0, 0);
    //
    //     let pilot_local_axisY = BABYLON.Mesh.CreateLines("pilot_local_axisY", [
    //         new BABYLON.Vector3.Zero(), new BABYLON.Vector3(0, size, 0), new BABYLON.Vector3(-0.05 * size, size * 0.95, 0),
    //         new BABYLON.Vector3(0, size, 0), new BABYLON.Vector3(0.05 * size, size * 0.95, 0)
    //     ], Game.CURRENT_SCENE);
    //     pilot_local_axisY.color = new BABYLON.Color3(0, 1, 0);
    //
    //     let pilot_local_axisZ = BABYLON.Mesh.CreateLines("pilot_local_axisZ", [
    //         new BABYLON.Vector3.Zero(), new BABYLON.Vector3(0, 0, size), new BABYLON.Vector3( 0 , -0.05 * size, size * 0.95),
    //         new BABYLON.Vector3(0, 0, size), new BABYLON.Vector3( 0, 0.05 * size, size * 0.95)
    //     ], Game.CURRENT_SCENE);
    //     pilot_local_axisZ.color = new BABYLON.Color3(0, 0, 1);
    //
    //     let local_origin = BABYLON.MeshBuilder.CreateBox("local_origin", {size:1}, Game.CURRENT_SCENE);
    //     local_origin.isVisible = false;
    //
    //     pilot_local_axisX.parent = local_origin;
    //     pilot_local_axisY.parent = local_origin;
    //     pilot_local_axisZ.parent = local_origin;
    //
    //     return local_origin;
    //
    // }
};