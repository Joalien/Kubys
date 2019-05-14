import * as GUI from 'babylonjs-gui';
import 'babylonjs-loaders'
import Map from "./Map";

export default class Player {

    static NAME_LABEL = {};
    static CURRENT_PLAYER_ID;
    static PLAYERS = {};

    static areObjectLoaded = false;
    static elf;
    static dwarf;
    static assassin;
    static wizard = BABYLON.AbstractMesh;
    static berserker;


    constructor(characteristics)
    {

        if(!Player.areObjectLoaded) {
            console.log("load asset");
            BABYLON.SceneLoader.LoadAssetContainer("/resources/wizard/", "wizard.obj", Map.SCENE, function (container) {
                Player.wizard = BABYLON.Mesh.MergeMeshes(container.meshes, true, true, undefined, false, true);
                console.log("Loading wizard ...");
                console.log("scaling factor : " + Player.getScalingVectorToFit(Player.wizard));
                let scaleFactor = Player.getScalingVectorToFit(Player.wizard);
                scaleFactor = Math.min(scaleFactor.x, scaleFactor.y, scaleFactor.z);
                Player.wizard.scaling = new BABYLON.Vector3(scaleFactor, scaleFactor, scaleFactor);
                // Player.wizard.dispose();
                console.log(new BABYLON.Mesh(Player.wizard));
                console.log("wizard : "+Player.wizard);

            });
            BABYLON.SceneLoader.LoadAssetContainer("/resources/elf/", "elf.obj", Map.SCENE, function (container) {
                Player.elf = BABYLON.Mesh.MergeMeshes(container.meshes);
                Player.resize(Player.elf.scaling);

            });
            BABYLON.SceneLoader.LoadAssetContainer("/resources/dwarf/", "dwarf.obj", Map.SCENE, function (container) {
                Player.dwarf = BABYLON.Mesh.MergeMeshes(container.meshes);
                Player.resize(Player.dwarf.scaling);

            });
            BABYLON.SceneLoader.LoadAssetContainer("/resources/assassin/", "assassin.obj", Map.SCENE, function (container) {
                Player.assassin = BABYLON.Mesh.MergeMeshes(container.meshes);
                Player.resize(Player.assassin.scaling);

            });
            BABYLON.SceneLoader.LoadAssetContainer("/resources/berserker/", "berserker.obj", Map.SCENE, function (container) {
                Player.berserker = BABYLON.Mesh.MergeMeshes(container.meshes);
                Player.resize(Player.berserker.scaling);

            });
            Player.areObjectLoaded = true;
            return;
        }


        Player.PLAYERS[characteristics.id] = characteristics;
        // Appel des variables nécéssaires
        const cubeSize = 1;
        const playerSize = 1;

        Player.CURRENT_PLAYER_ID = characteristics.id;

        let mesh;

        switch (characteristics.breed){
            case "DWARF":
                mesh = Player.wizard.clone();
                console.log("dwarf : "+mesh);
                break;
            case "ELF":
                mesh = Player.wizard.clone();
                break;
            case "ASSASSIN":
                mesh = Player.wizard.clone();
                break;
            case "WIZARD":
                mesh = Player.wizard.clone();
                break;
            case "BERSERKER":
                mesh = Player.wizard.clone();
                break;

        }
        mesh.position = new BABYLON.Vector3(0, cubeSize + playerSize / 2 , 0);
        mesh.scaling = new BABYLON.Vector3(0.95, playerSize*0.95, 0.95);

        let advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("UI");
        let rect1 = new BABYLON.GUI.Rectangle(characteristics.id);
        rect1.width = 0.1;
        rect1.height = "40px";
        rect1.cornerRadius = 20;
        rect1.color = "blue";
        rect1.thickness = 4;
        advancedTexture.addControl(rect1);

        this.label = new BABYLON.GUI.TextBlock();
        this.label.text = "Error";
        rect1.addControl(this.label);

        rect1.linkWithMesh(mesh);
        rect1.linkOffsetY = -30;

        Player.NAME_LABEL[mesh] = rect1;

        this.mesh = mesh;
    }

    setLabel (label){
        this.label.text = label;
    }

    setPosition (position){
        this.mesh.position = position;
    }


    //TODO ! (https://www.babylonjs-playground.com/#7BICDZ)
    static getScalingVectorToFit (mesh) {
        let otherVector = BABYLON.Vector3.One();
        if(!mesh.__scaleVectorCache){
            mesh.__scaleVectorCache = BABYLON.Vector3.Zero();
        }

        let size = Player.getAbsoluteSize(mesh);
        mesh.__scaleVectorCache.x = otherVector.x / size.x;
        mesh.__scaleVectorCache.y = otherVector.y / size.y;
        mesh.__scaleVectorCache.z = otherVector.z / size.z;

        return mesh.__scaleVectorCache;
    };



    static getAbsoluteSize(mesh) {
        if(!mesh.__size){
            mesh.__size = BABYLON.Vector3.Zero();
        }

        let bounds = mesh.getBoundingInfo();
        mesh.__size.x = Math.abs(bounds.minimum.x - bounds.maximum.x);
        mesh.__size.y = Math.abs(bounds.minimum.y - bounds.maximum.y);
        mesh.__size.z = Math.abs(bounds.minimum.z - bounds.maximum.z);

        return mesh.__size;
    };


};