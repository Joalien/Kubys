import { Rectangle, AdvancedDynamicTexture, TextBlock } from 'babylonjs-gui'
import 'babylonjs-loaders'
import Game from "./Game";
import Communication from "./Communication";

export default class Player {

    static NAME_LABEL = {};
    static CURRENT_PLAYER_ID;
    static PLAYERS = {};
    static advancedTexture;

    static areObjectLoaded = false;
    static elf;
    static dwarf;
    static assassin;
    static wizard;
    static berserker;

    static init(callback) {
        Player.advancedTexture = AdvancedDynamicTexture.CreateFullscreenUI("UI");
        if(!Player.areObjectLoaded) {
            Player.areObjectLoaded = true;
            console.log("load asset");
            BABYLON.SceneLoader.LoadAssetContainer("/resources/objects/wizard/", "wizard.obj", Game.CURRENT_SCENE, function (container) {
                Player.wizard = BABYLON.Mesh.MergeMeshes(container.meshes, true, true, undefined, false, true);
                Player.wizard.visibility = 0;

                let scaleFactor = Player.getScalingVectorToFit(Player.wizard);
                scaleFactor = Math.min(scaleFactor.x, scaleFactor.y, scaleFactor.z);
                Player.wizard.scaling = new BABYLON.Vector3(scaleFactor, scaleFactor, scaleFactor);
                console.log('Texture loaded !');
                // TODO Improve me !
                callback();
            });
            // BABYLON.SceneLoader.LoadAssetContainer("/resources/centor/", "cent.obj", Game.CURRENT_SCENE, function (container) {
            //
            //
            // Player.elf = BABYLON.Mesh.MergeMeshes(container.meshes, true, true, undefined, false, true);
            // Player.elf.visibility = 0;

            // Player.elf = container.meshes[1];
            // console.log(Player.elf);
            // container.addAllToScene();

            // let scaleFactor = Player.getScalingVectorToFit(Player.elf);
            // scaleFactor = Math.min(scaleFactor.x, scaleFactor.y, scaleFactor.z);
            // Player.elf.scaling = new BABYLON.Vector3(scaleFactor, scaleFactor, scaleFactor);
            //
            // });
            // BABYLON.SceneLoader.LoadAssetContainer("/resources/dwarf/", "dwarf.obj", Game.CURRENT_SCENE, function (container) {
            //     Player.dwarf = BABYLON.Mesh.MergeMeshes(container.meshes);
            //     Player.resize(Player.dwarf.scaling);
            //
            // });
            // BABYLON.SceneLoader.LoadAssetContainer("/resources/assassin/", "assassin.obj", Game.CURRENT_SCENE, function (container) {
            //     Player.assassin = BABYLON.Mesh.MergeMeshes(container.meshes);
            //     Player.resize(Player.assassin.scaling);
            //
            // });
            // BABYLON.SceneLoader.LoadAssetContainer("/resources/berserker/", "berserker.obj", Game.CURRENT_SCENE, function (container) {
            //     Player.berserker = BABYLON.Mesh.MergeMeshes(container.meshes);
            //     Player.resize(Player.berserker.scaling);
            //
            // });
        }
    }

    constructor(characteristics)
    {
        Player.PLAYERS[characteristics.id] = characteristics;
        // Appel des variables nécéssaires
        const cubeSize = 1;
        const playerSize = 1;

        // Player.CURRENT_PLAYER_ID = characteristics.id;

        let mesh;

        switch (characteristics.breed) {
            case "DWARF":
                // mesh = Player.dwarf.clone();
                mesh = Player.wizard.clone();
                break;
            case "ELF":
                // mesh = Player.elf.clone();
                mesh = Player.wizard.clone();
                break;
            case "ASSASSIN":
                // mesh = Player.assassin.clone();
                mesh = Player.wizard.clone();
                break;
            case "WIZARD":
                mesh = Player.wizard.clone();
                break;
            case "BERSERKER":
                // mesh = Player.berserker.clone();
                mesh = Player.wizard.clone();
                break;
            default :
                console.error("Breed not found !");
                break;
        }
        mesh.id = characteristics.id;
        mesh.visibility = 1;


        let rect1 = new Rectangle("rect"+characteristics.id);
        rect1.id = "rect"+characteristics.id;
        rect1.width = (characteristics.name.length+1) * 10 + "px";
        rect1.height = "40px";
        rect1.cornerRadius = 20;
        rect1.color = "blue";
        rect1.thickness = 4;
        Player.advancedTexture.addControl(rect1);

        this.label = new TextBlock();
        this.label.text = characteristics.name;
        rect1.addControl(this.label);

        rect1.linkWithMesh(mesh);
        rect1.linkOffsetY = -70;

        Player.NAME_LABEL["rect"+characteristics.id] = rect1;

        this.mesh = mesh;

        return this;
    }

    setPosition (position) {
        this.mesh.position = position;
    }


    static getScalingVectorToFit (mesh) {
        let otherVector = BABYLON.Vector3.One();
        if(!mesh.__scaleVectorCache) {
            mesh.__scaleVectorCache = BABYLON.Vector3.Zero();
        }

        let size = Player.getAbsoluteSize(mesh);
        mesh.__scaleVectorCache.x = otherVector.x / size.x;
        mesh.__scaleVectorCache.y = otherVector.y / size.y;
        mesh.__scaleVectorCache.z = otherVector.z / size.z;

        return mesh.__scaleVectorCache;
    };


    static getAbsoluteSize(mesh) {
        if(!mesh.__size) {
            mesh.__size = BABYLON.Vector3.Zero();
        }

        let bounds = mesh.getBoundingInfo();
        mesh.__size.x = Math.abs(bounds.minimum.x - bounds.maximum.x);
        mesh.__size.y = Math.abs(bounds.minimum.y - bounds.maximum.y);
        mesh.__size.z = Math.abs(bounds.minimum.z - bounds.maximum.z);

        return mesh.__size;
    };

    static refreshPlayer(player) {
        Player.CURRENT_PLAYER_ID = player.id;

        Communication.sendMessage("/getAllMap", null);
        Communication.sendMessage("/getSpells", null);

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