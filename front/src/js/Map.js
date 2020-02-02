import "babylonjs-loaders";
import Grass from "../../resources/textures/grass.jpg"
import Tree from "../../resources/textures/tree.jpg"
import Leaf from "../../resources/textures/leaf.jpg"
import Player from "./Player";
import Gui from "./Gui";
import Game from "./Game";

import Communication from "./Communication"

export default class Map {

    static ringPlayers = [];

    constructor(scene, camera) {
        // Appel des letiables nécéssaires
        this.cubeSize = 1;
        this.camera = camera;
        this.scene = scene;

        //Uncomment to see axis (debug purpose)
        this.showWorldAxis(1);

        // Création de notre lumière principale
        let light = new BABYLON.HemisphericLight("light1", new BABYLON.Vector3(0, 1, 0), this.scene);
        light.diffuse = new BABYLON.Color3(1, 1, 1);
        light.specular = new BABYLON.Color3(1, 1, 1);
        light.intensity = 1;

        // Skybox
        let skybox = BABYLON.MeshBuilder.CreateBox("skyBox", {size:1000.0}, this.scene);
        let skyboxMaterial = new BABYLON.StandardMaterial("skyBox", this.scene);
        skyboxMaterial.backFaceCulling = false;
        skyboxMaterial.reflectionTexture = new BABYLON.CubeTexture("resources/textures/skybox", this.scene);
        skyboxMaterial.reflectionTexture.coordinatesMode = BABYLON.Texture.SKYBOX_MODE;
        skyboxMaterial.diffuseColor = new BABYLON.Color3(0, 0, 0);
        skyboxMaterial.specularColor = new BABYLON.Color3(0, 0, 0);
        skybox.material = skyboxMaterial;
        skybox.infiniteDistance = true;
    }

    getAllMap = async message => {
        console.log("GET ALL MAP !!!");
        // called when the client receives a STOMP message from the server
        if (message.body) {
            Communication.getAllMapSubscription.unsubscribe();
            //For each item in the map, we print it
            for (let player of JSON.parse(message.body)) {
                if (player.hasOwnProperty("breed")) {// Check if player could be optimized
                    let objPlayer = await Player.build(player);
                    objPlayer.setPosition(new BABYLON.Vector3(player.position.x, player.position.y, player.position.z));
                } else Game.CURRENT_SCENE.MAP.createLandPlot(player.position.x, player.position.y ,player.position.z);
            }
        } else {
            console.log("got empty message");
        }
    };

    updateMap = async message => {
        console.log("update the map");
        if (message.body) {
            let player = JSON.parse(message.body);
            let mesh = this.scene.getMeshByID(player.id);

            if (mesh == null) { //If new player
                let objPlayer = await Player.build(player);
                objPlayer.setLabel(player.name);
                objPlayer.setPosition(new BABYLON.Vector3(player.position.x, player.position.y, player.position.z));

                //TODO Create new endpoint for disconnected users
            // } else if(player.connected === false) {// If player disconnect
            //     console.log("Player "+player.id+" has left the game");
            //     Player.NAME_LABEL[mesh].dispose();
            //     mesh.dispose();
            } else {//If player had already been created and should move (normal case)
                let newPosition = new BABYLON.Vector3(player.position.x, player.position.y, player.position.z);
                // try to find the direction in order to rotate the player around y axis

                if(newPosition.x - mesh.position.x === 1) {
                    mesh.rotation.y = Math.PI / 2;
                } else if(newPosition.x - mesh.position.x === -1) {
                    mesh.rotation.y = - Math.PI / 2;
                } else if(newPosition.z - mesh.position.z === 1) {
                    mesh.rotation.y = 0;
                } else if(newPosition.z - mesh.position.z === -1) {
                    mesh.rotation.y = Math.PI;
                }

                let animationBox = new BABYLON.Animation("translatePlayer", "position", 500, BABYLON.Animation.ANIMATIONTYPE_VECTOR3, BABYLON.Animation.ANIMATIONLOOPMODE_CONSTANT);
                let keys = [];

                keys.push({
                    frame: 0,
                    value: mesh.position
                });

                keys.push({
                    frame: 100,
                    value: newPosition
                });
                animationBox.setKeys(keys);
                mesh.animations = [];
                mesh.animations.push(animationBox);
                mesh.position = newPosition;
                this.scene.beginAnimation(mesh, 0, 100, true);
            }
        } else {
            console.log("got empty message, maybe player can't move");
        }
    };

    createFloor() {
        // SUR TOUS LES AXES Y -> On monte les meshes de la moitié de la hauteur du mesh en question.
        let mainBox = this.createLandPlot(this.scene, 0, super.cubeSize/2, 0);

        const sizeOfMap = 50;

        for (let i = 0; i < sizeOfMap; i++) {
            for (let j = 0; j < sizeOfMap; j++) {
                let secondaryBox = mainBox.clone();
                secondaryBox.position = new BABYLON.Vector3(-sizeOfMap / 2 * super.cubeSize + super.cubeSize * i, super.cubeSize / 2, -sizeOfMap / 2 * super.cubeSize + super.cubeSize * j);
            }
        }
    }

    createTree(x, z) {
        let treeTexture = new BABYLON.Texture(Tree, this.scene);
        let leafTexture = new BABYLON.Texture(Leaf, this.scene);
        //Creation of a material
        const treeMaterial = new BABYLON.StandardMaterial("treeTexture", this.scene);
        treeMaterial.diffuseTexture = treeTexture;
        const leafMaterial = new BABYLON.StandardMaterial("leafTexture", this.scene);
        leafMaterial.diffuseTexture = leafTexture;

        // SUR TOUS LES AXES Y -> On monte les meshes de la moitié de la hauteur du mesh en question.
        let tree = BABYLON.Mesh.CreateBox("tree", super.cubeSize, this.scene);
        tree.position = new BABYLON.Vector3(x, super.cubeSize / 2 + super.cubeSize, z);
        tree.scaling = new BABYLON.Vector3(0.95, 0.95, 0.95);
        tree.material = treeMaterial;
        for (let i = 1; i < 4; i++) {
            let secondaryBox = tree.clone();
            secondaryBox.position.y = tree.position.y + super.cubeSize * i;
        }

        let leaf = BABYLON.Mesh.CreateBox("leaf", super.cubeSize, this.scene);
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

    createLandPlot(x, y, z) {
        let tex = new BABYLON.Texture(Grass, this.scene);
        //Creation of a material
        const grassMaterial = new BABYLON.StandardMaterial("groundTexture", this.scene);
        grassMaterial.diffuseTexture = tex;

        let mainBox = BABYLON.Mesh.CreateBox("box1", super.cubeSize, this.scene);
        mainBox.position = new BABYLON.Vector3(x, y, z);
        mainBox.scaling = new BABYLON.Vector3(0.95, 0.95, 0.95);
        mainBox.material = grassMaterial;
    }

    selectionRing = async message => {
        if (message.body) {
            let i = 0;
            //For each item in the map, we print i
            for (let player of JSON.parse(message.body)) {
                let alpha = (2 * Math.PI / JSON.parse(message.body).length * i) - Math.PI/2;
                let distance = 3;

                let objPlayer = await Player.build(player);
                objPlayer.mesh.position = new BABYLON.Vector3(Math.cos(alpha)*distance, 0, Math.sin(alpha)*distance);
                objPlayer.mesh.rotate(BABYLON.Axis.Y, Math.PI / 2, BABYLON.Space.WORLD);
                objPlayer.mesh.rotate(BABYLON.Axis.Y, -alpha, BABYLON.Space.WORLD);

                let animationBox = new BABYLON.Animation("translatePlayer", "position", 500, BABYLON.Animation.ANIMATIONTYPE_VECTOR3, BABYLON.Animation.ANIMATIONLOOPMODE_CONSTANT);
                let keys = [];
                keys.push({
                    frame: 0,
                    value: objPlayer.mesh.position
                });
                keys.push({
                    frame: 100,
                    value: new BABYLON.Vector3.Zero()
                });

                animationBox.setKeys(keys);
                objPlayer.mesh.animations = [];
                objPlayer.mesh.animations.push(animationBox);

                Map.ringPlayers[i] = objPlayer.mesh;
                i++;
            }

            this.scene.onPointerObservable.add((evt) => {
                if (evt.type === BABYLON.PointerEventTypes.POINTERDOWN) {
                    let pickInfo = this.scene.pick(this.scene.pointerX,this.scene.pointerY,null,null).pickedMesh;
                    if(pickInfo.animations.length === 0) return;
                    let numberOfPlayer;
                    for (let i=0; i<Map.ringPlayers.length; i++) {
                        if(Map.ringPlayers[i].position.equals(new BABYLON.Vector3.Zero())) {
                            this.scene.beginAnimation(Map.ringPlayers[i], 100, 0, true);
                            Game.MAIN_SCENE.GUI.removePlayButton();
                            if (Map.ringPlayers[i] === pickInfo) return;// If we click against on same mesh
                        }
                        if(Map.ringPlayers[i] === pickInfo) {
                            numberOfPlayer = i;
                            console.log(pickInfo);
                            Game.CURRENT_SCENE.GUI.addPlayButton(i);
                        }
                    }

                    let animationCamera = new BABYLON.Animation("translateCamera", "alpha", 120, BABYLON.Animation.ANIMATIONTYPE_FLOAT, BABYLON.Animation.ANIMATIONLOOPMODE_CONSTANT);
                    let keys = [];
                    keys.push({
                        frame: 0,
                        value: this.camera.arcRotateCamera.alpha
                    });
                    keys.push({
                        frame: 100,
                        value:(2 * Math.PI / JSON.parse(message.body).length * numberOfPlayer) - Math.PI/2
                    });
                    animationCamera.setKeys(keys);
                    this.camera.arcRotateCamera.animations = [];
                    this.camera.arcRotateCamera.animations.push(animationCamera);
                    this.scene.beginAnimation(this.camera.arcRotateCamera, 0, 100, false, 1, () =>
                        this.scene.beginAnimation(pickInfo, 0, 100, true)
                    );
                }
            });
        }
    };

    clearRingSelection = () => {
        for (let mesh of Map.ringPlayers) {
            mesh.dispose();
            Player.NAME_LABEL["rect"+mesh.id].dispose();
        }
    };

    showWorldAxis(size) {
        let makeTextPlane = (text, color, size) => {
            let dynamicTexture = new BABYLON.DynamicTexture("DynamicTexture", 50, this.scene, true);
            dynamicTexture.hasAlpha = true;
            dynamicTexture.drawText(text, 5, 40, "bold 36px Arial", color , "transparent", true);
            let plane = BABYLON.Mesh.CreatePlane("TextPlane", size, this.scene, true);
            plane.material = new BABYLON.StandardMaterial("TextPlaneMaterial", this.scene);
            plane.material.backFaceCulling = false;
            plane.material.specularColor = new BABYLON.Color3(0, 0, 0);
            plane.material.diffuseTexture = dynamicTexture;
            return plane;
        };
        let axisX = BABYLON.Mesh.CreateLines("axisX", [
            BABYLON.Vector3.Zero(), new BABYLON.Vector3(size, 0, 0), new BABYLON.Vector3(size * 0.95, 0.05 * size, 0),
            new BABYLON.Vector3(size, 0, 0), new BABYLON.Vector3(size * 0.95, -0.05 * size, 0)
        ], this.scene);
        axisX.color = new BABYLON.Color3(1, 0, 0);
        let xChar = makeTextPlane("X", "red", size / 10);
        xChar.position = new BABYLON.Vector3(0.9 * size, -0.05 * size, 0);
        let axisY = BABYLON.Mesh.CreateLines("axisY", [
            BABYLON.Vector3.Zero(), new BABYLON.Vector3(0, size, 0), new BABYLON.Vector3( -0.05 * size, size * 0.95, 0),
            new BABYLON.Vector3(0, size, 0), new BABYLON.Vector3( 0.05 * size, size * 0.95, 0)
        ], this.scene);
        axisY.color = new BABYLON.Color3(0, 1, 0);
        let yChar = makeTextPlane("Y", "green", size / 10);
        yChar.position = new BABYLON.Vector3(0, 0.9 * size, -0.05 * size);
        let axisZ = BABYLON.Mesh.CreateLines("axisZ", [
            BABYLON.Vector3.Zero(), new BABYLON.Vector3(0, 0, size), new BABYLON.Vector3( 0 , -0.05 * size, size * 0.95),
            new BABYLON.Vector3(0, 0, size), new BABYLON.Vector3( 0, 0.05 * size, size * 0.95)
        ], this.scene);
        axisZ.color = new BABYLON.Color3(0, 0, 1);
        let zChar = makeTextPlane("Z", "blue", size / 10);
        zChar.position = new BABYLON.Vector3(0, 0.05 * size, 0.9 * size);
    };

};
