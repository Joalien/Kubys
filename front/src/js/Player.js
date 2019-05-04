import * as GUI from 'babylonjs-gui';
import Communication from './Communication';

export default class Player {

    constructor(scene, id)
    {
        // Appel des variables nécéssaires
        const cubeSize = 1;
        const playerSize = 1;

        // SUR TOUS LES AXES Y -> On monte les meshes de la moitié de la hauteur du mesh en question.
        let player = BABYLON.Mesh.CreateBox(id, cubeSize, scene);
        let playerColor = new BABYLON.StandardMaterial('red', scene);
        playerColor.diffuseColor = new BABYLON.Color3(1, 0, 0);
        player.position = new BABYLON.Vector3(0, cubeSize + playerSize / 2 , 0);
        player.scaling = new BABYLON.Vector3(0.95, playerSize*0.95, 0.95);
        player.material = playerColor;

        let advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("UI");
        let rect1 = new BABYLON.GUI.Rectangle();
        rect1.width = 0.1;
        rect1.height = "40px";
        rect1.cornerRadius = 20;
        rect1.color = "blue";
        rect1.thickness = 4;
        advancedTexture.addControl(rect1);

        this.label = new BABYLON.GUI.TextBlock();
        this.label.text = "Joalien";
        rect1.addControl(this.label);

        rect1.linkWithMesh(player);
        rect1.linkOffsetY = -30;

        window.addEventListener("keypress", function (evt) {
            switch (evt.key) {
                case 'z':
                case 's':
                case 'q':
                case 'd':
                    Communication.clientSocket.send("/move", {}, JSON.stringify(evt.key));
            }

        }, false);

        this.player = player;
    }

    setlabel (label){
        this.label.text = label;
    }

    getlabel (){
        return this.label.text;
    }

    getPlayer(){
        return this.player;
    }

    setPosition (position){
        this.player.position = position;
    }

};