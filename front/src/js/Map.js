import "babylonjs-loaders";
import Grass from "../../textures/grass.jpg"
import Tree from "../../textures/tree.jpg"
import Leaf from "../../textures/leaf.jpg"

export default class Map {

    static SCENE;

    constructor(game) {
        // Appel des variables nécéssaires
        this.cubeSize = 1;
        this.game = game;
        Map.SCENE = game.scene;

        // Création de notre lumière principale
        let light = new BABYLON.HemisphericLight("light1", new BABYLON.Vector3(0, 1, 0), game.scene);
        light.diffuse = new BABYLON.Color3(1, 1, 1);
        light.specular = new BABYLON.Color3(1, 1, 1);
        light.intensity = 1;


        // Skybox
        let skybox = BABYLON.MeshBuilder.CreateBox("skyBox", {size:1000.0}, game.scene);
        let skyboxMaterial = new BABYLON.StandardMaterial("skyBox", game.scene);
        skyboxMaterial.backFaceCulling = false;
        skyboxMaterial.reflectionTexture = new BABYLON.CubeTexture("textures/skybox", game.scene);
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
                
        // Map.createFloor();
        // Map.createTree( -15, 5);
        // Map.createTree( 2, -10);
        // Map.createLandPlot( 0, 0, 0);
        // Map.createLandPlot( 0, 0, 1);
        // Map.createLandPlot( 1, 0, 0);
        // Map.createLandPlot( 1, 0, 1);

    }

    static createFloor(){

        // SUR TOUS LES AXES Y -> On monte les meshes de la moitié de la hauteur du mesh en question.
        let mainBox = this.createLandPlot(this.SCENE, 0, super.cubeSize/2, 0);

        const sizeOfMap = 50;

        for (let i = 0; i < sizeOfMap; i++) {
            for (let j = 0; j < sizeOfMap; j++) {
                let secondaryBox = mainBox.clone();
                secondaryBox.position = new BABYLON.Vector3(-sizeOfMap / 2 * super.cubeSize + super.cubeSize * i, super.cubeSize / 2, -sizeOfMap / 2 * super.cubeSize + super.cubeSize * j);
            }
        }
    }

    static createTree( x, z){
        let treeTexture = new BABYLON.Texture(Tree, this.SCENE);
        let leafTexture = new BABYLON.Texture(Leaf, this.SCENE);
        //Creation of a material
        const treeMaterial = new BABYLON.StandardMaterial("treeTexture", this.SCENE);
        treeMaterial.diffuseTexture = treeTexture;
        const leafMaterial = new BABYLON.StandardMaterial("leafTexture", this.SCENE);
        leafMaterial.diffuseTexture = leafTexture;

        // SUR TOUS LES AXES Y -> On monte les meshes de la moitié de la hauteur du mesh en question.
        let tree = BABYLON.Mesh.CreateBox("tree", super.cubeSize, this.SCENE);
        tree.position = new BABYLON.Vector3(x, super.cubeSize / 2 + super.cubeSize, z);
        tree.scaling = new BABYLON.Vector3(0.95, 0.95, 0.95);
        tree.material = treeMaterial;
        for (let i = 1; i < 4; i++) {
            let secondaryBox = tree.clone();
            secondaryBox.position.y = tree.position.y + super.cubeSize * i;
        }

        let leaf = BABYLON.Mesh.CreateBox("leaf", super.cubeSize, this.SCENE);
        leaf.position = new BABYLON.Vector3(x, super.cubeSize / 2 + super.cubeSize + super.cubeSize * 4, z);
        leaf.scaling = new BABYLON.Vector3(0.95, 0.95, 0.95);
        leaf.material = leafMaterial;
        for (let i = -1; i <= 1; i++) {
            for (let j = -1; j <= 1; j++) {
                if(i===0 && j===0) continue;
                let secondaryBox = leaf.clone();
                secondaryBox.position.x = x + i;
                secondaryBox.position.z = z + j;
                secondaryBox.position.y = leaf.position.y - super.cubeSize / 2;
            }
        }
    }

    static createLandPlot(x, y, z){
        let tex = new BABYLON.Texture(Grass, this.SCENE);
        //Creation of a material
        const grassMaterial = new BABYLON.StandardMaterial("groundTexture", this.SCENE);
        grassMaterial.diffuseTexture = tex;

        let mainBox = BABYLON.Mesh.CreateBox("box1", super.cubeSize, this.SCENE);
        mainBox.position = new BABYLON.Vector3(x, y, z);
        mainBox.scaling = new BABYLON.Vector3(0.95, 0.95, 0.95);
        mainBox.material = grassMaterial;
    }

};
