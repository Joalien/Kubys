import * as Materials from 'babylonjs-materials';
import "babylonjs-loaders";
import Grass from "../../textures/grass.jpg"

export default class Map {

    constructor(game) {
        // Appel des variables nécéssaires
        this.game = game;
        let scene = game.scene;
        const cubeSize = 1;

        // Création de notre lumière principale
        let light = new BABYLON.HemisphericLight("light1", new BABYLON.Vector3(0, 1, 0), scene);
        light.diffuse = new BABYLON.Color3(1, 1, 1);
        light.specular = new BABYLON.Color3(1, 1, 1);
        light.intensity = 1;


        // Skybox
        let skybox = BABYLON.MeshBuilder.CreateBox("skyBox", {size:1000.0}, scene);
        let skyboxMaterial = new BABYLON.StandardMaterial("skyBox", scene);
        skyboxMaterial.backFaceCulling = false;
        skyboxMaterial.reflectionTexture = new BABYLON.CubeTexture("textures/skybox", scene);
        skyboxMaterial.reflectionTexture.coordinatesMode = BABYLON.Texture.SKYBOX_MODE;
        skyboxMaterial.diffuseColor = new BABYLON.Color3(0, 0, 0);
        skyboxMaterial.specularColor = new BABYLON.Color3(0, 0, 0);
        skybox.material = skyboxMaterial;

        // let textureAssetTask = assetsManager.addCubeTextureTask("skyboxAsset", "textures/skybox");
        // assetsManager.load();
        // textureAssetTask.onSuccess = (task) => {
        //     console.log("onSuccess");
        //     skyboxMaterial.backFaceCulling = false;
        //     skyboxMaterial.reflectionTexture = new BABYLON.CubeTexture("textures/skybox", scene);
        //     skyboxMaterial.reflectionTexture.coordinatesMode = BABYLON.Texture.SKYBOX_MODE;
        //     skyboxMaterial.diffuseColor = new BABYLON.Color3(0, 0, 0);
        //     skyboxMaterial.specularColor = new BABYLON.Color3(0, 0, 0);
        //     // skyboxMaterial.disableLighting = false;
        //     // skyboxMaterial.fogEnabled = f    alse;
        //     // skybox.material = skyboxMaterial;
        //     // skybox.infiniteDistance = true;
        // };
        // textureAssetTask.onError = function (){
        //     console.log("onError");
        // };


        let tex = new BABYLON.Texture(Grass, scene);
        //Creation of a material
        const grassMaterial = new BABYLON.StandardMaterial("groundTexture", scene);
        grassMaterial.diffuseTexture = tex;

        // SUR TOUS LES AXES Y -> On monte les meshes de la moitié de la hauteur du mesh en question.
        let mainBox = BABYLON.Mesh.CreateBox("box1", cubeSize, scene);
        mainBox.position = new BABYLON.Vector3(0, cubeSize / 2, 0);
        mainBox.scaling = new BABYLON.Vector3(0.95, 0.95, 0.95);
        mainBox.material = grassMaterial;

        const sizeOfMap = 50;

        for (let i = 0; i < sizeOfMap; i++) {
            for (let j = 0; j < sizeOfMap; j++) {
                let secondaryBox = mainBox.clone();
                secondaryBox.position = new BABYLON.Vector3(-sizeOfMap / 2 * cubeSize + cubeSize * i, cubeSize / 2, -sizeOfMap / 2 * cubeSize + cubeSize * j);
            }
        }
    }

};
