import * as GUI from 'babylonjs-gui';
import Communication from './Communication';

export default class Player {

    static hashmap = {};

    constructor(scene, characteristics)
    {
        // Appel des variables nécéssaires
        const cubeSize = 1;
        const playerSize = 1;

        // SUR TOUS LES AXES Y -> On monte les meshes de la moitié de la hauteur du mesh en question.
        let mesh = BABYLON.Mesh.CreateBox(characteristics.id, cubeSize, scene);
        let playerColor = new BABYLON.StandardMaterial('red', scene);
        switch (characteristics.breed){
            case "DWARF":
                playerColor.diffuseColor = new BABYLON.Color3(1, 0, 0);
                break;
            case "ELF":
                playerColor.diffuseColor = new BABYLON.Color3(0, 0, 1);
                break;
            case "ASSASSIN":
                playerColor.diffuseColor = new BABYLON.Color3(1, 0, 1);
                break;
            case "WIZARD":
                playerColor.diffuseColor = new BABYLON.Color3(0, 1, 1);
                break;
            case "BERSERKER":
                playerColor.diffuseColor = new BABYLON.Color3(1, 1, 0);
                break;

        }
        mesh.position = new BABYLON.Vector3(0, cubeSize + playerSize / 2 , 0);
        mesh.scaling = new BABYLON.Vector3(0.95, playerSize*0.95, 0.95);
        mesh.material = playerColor;

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

        Player.hashmap[mesh] = rect1;

        this.mesh = mesh;
    }

    setLabel (label){
        this.label.text = label;
    }

    setPosition (position){
        this.mesh.position = position;
    }

};