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
    static wizard;
    static berserker;


    constructor(characteristics)
    {

        if(!Player.areObjectLoaded) {
            console.log("load asset");


            BABYLON.SceneLoader.LoadAssetContainer("/resources/wizard/", "wizard2.obj", Map.SCENE, function (container) {

                Player.wizard = BABYLON.Mesh.MergeMeshes(container.meshes, true, true, undefined, false, true);
                Player.wizard.visibility = 0;

                let scaleFactor = Player.getScalingVectorToFit(Player.wizard);
                scaleFactor = Math.min(scaleFactor.x, scaleFactor.y, scaleFactor.z);
                Player.wizard.scaling = new BABYLON.Vector3(scaleFactor, scaleFactor, scaleFactor);

            });
            // BABYLON.SceneLoader.LoadAssetContainer("/resources/centor/", "cent.obj", Map.SCENE, function (container) {
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
            // BABYLON.SceneLoader.LoadAssetContainer("/resources/dwarf/", "dwarf.obj", Map.SCENE, function (container) {
            //     Player.dwarf = BABYLON.Mesh.MergeMeshes(container.meshes);
            //     Player.resize(Player.dwarf.scaling);
            //
            // });
            // BABYLON.SceneLoader.LoadAssetContainer("/resources/assassin/", "assassin.obj", Map.SCENE, function (container) {
            //     Player.assassin = BABYLON.Mesh.MergeMeshes(container.meshes);
            //     Player.resize(Player.assassin.scaling);
            //
            // });
            // BABYLON.SceneLoader.LoadAssetContainer("/resources/berserker/", "berserker.obj", Map.SCENE, function (container) {
            //     Player.berserker = BABYLON.Mesh.MergeMeshes(container.meshes);
            //     Player.resize(Player.berserker.scaling);
            //
            // });
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
        }

        mesh.id = characteristics.id;
        mesh.visibility = 1;

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

        return this;
    }

    setLabel (label){
        this.label.text = label;
    }

    setPosition (position){
        this.mesh.position = position;
    }


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

    // //Local Axes
    // static localAxes(size) {
    //     let pilot_local_axisX = BABYLON.Mesh.CreateLines("pilot_local_axisX", [
    //         new BABYLON.Vector3.Zero(), new BABYLON.Vector3(size, 0, 0), new BABYLON.Vector3(size * 0.95, 0.05 * size, 0),
    //         new BABYLON.Vector3(size, 0, 0), new BABYLON.Vector3(size * 0.95, -0.05 * size, 0)
    //     ], Map.SCENE);
    //     pilot_local_axisX.color = new BABYLON.Color3(1, 0, 0);
    //
    //     let pilot_local_axisY = BABYLON.Mesh.CreateLines("pilot_local_axisY", [
    //         new BABYLON.Vector3.Zero(), new BABYLON.Vector3(0, size, 0), new BABYLON.Vector3(-0.05 * size, size * 0.95, 0),
    //         new BABYLON.Vector3(0, size, 0), new BABYLON.Vector3(0.05 * size, size * 0.95, 0)
    //     ], Map.SCENE);
    //     pilot_local_axisY.color = new BABYLON.Color3(0, 1, 0);
    //
    //     let pilot_local_axisZ = BABYLON.Mesh.CreateLines("pilot_local_axisZ", [
    //         new BABYLON.Vector3.Zero(), new BABYLON.Vector3(0, 0, size), new BABYLON.Vector3( 0 , -0.05 * size, size * 0.95),
    //         new BABYLON.Vector3(0, 0, size), new BABYLON.Vector3( 0, 0.05 * size, size * 0.95)
    //     ], Map.SCENE);
    //     pilot_local_axisZ.color = new BABYLON.Color3(0, 0, 1);
    //
    //     let local_origin = BABYLON.MeshBuilder.CreateBox("local_origin", {size:1}, Map.SCENE);
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