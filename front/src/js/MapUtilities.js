import Player from "./Player";
import Tree from "../../resources/textures/tree.jpg";
import Leaf from "../../resources/textures/leaf.jpg";
import Grass from "../../resources/textures/grass.jpg";
import Communication from "./Communication";

export default class MapUtilities {

    static CUBE_SIZE = 1;

    static getAllMap = async (message, scene) => {
        // called when the client receives a STOMP message from the server
        if (message.body) {
            for (let player of JSON.parse(message.body)) {
                if (player.hasOwnProperty("characteristics")) {// TODO Improve me
                    await this.createPlayer(player, scene);
                } else {
                    this.createLandPlot(player.position.x, player.position.y, player.position.z);
                }
            }
        } else {
            console.log("got empty message");
        }
    };

    static async createPlayer(player, scene) {
        let objPlayer = await Player.build(player, scene);
        objPlayer.setPosition(new BABYLON.Vector3(player.position.x, player.position.y, player.position.z));
    }

    static updateMap = async (message, scene) => {
        if (message.body) {
            let player = JSON.parse(message.body);
            let mesh = scene.getMeshByID(player.id);

            let isNewPlayer = mesh === null;
            if (isNewPlayer) {
                await this.createPlayer(player, scene);
            } else if (player.connected === false) { // If player disconnect
                console.log("Player " + player.id + " has left the game");
                Player.playersNameMap.get(player.id).dispose();
                mesh.dispose();
            } else { //If player had already been created and should move (normal case)
                let newPosition = new BABYLON.Vector3(player.position.x, player.position.y, player.position.z);
                if (newPosition.x - mesh.position.x === 1) {
                    mesh.rotation.y = Math.PI / 2;
                } else if (newPosition.x - mesh.position.x === -1) {
                    mesh.rotation.y = -Math.PI / 2;
                } else if (newPosition.z - mesh.position.z === 1) {
                    mesh.rotation.y = 0;
                } else if (newPosition.z - mesh.position.z === -1) {
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
                scene.beginAnimation(mesh, 0, 100, true);
            }
        } else {
            console.log("got empty message, maybe player can't move");
        }
    };

    static createFloor(scene) {
        // SUR TOUS LES AXES Y -> On monte les meshes de la moitié de la hauteur du mesh en question.
        let mainBox = this.createLandPlot(scene, 0, MapUtilities.CUBE_SIZE / 2, 0);

        const sizeOfMap = 50;

        for (let i = 0; i < sizeOfMap; i++) {
            for (let j = 0; j < sizeOfMap; j++) {
                let secondaryBox = mainBox.clone();
                secondaryBox.position = new BABYLON.Vector3(
                    -sizeOfMap / 2 * MapUtilities.CUBE_SIZE + MapUtilities.CUBE_SIZE * i,
                    MapUtilities.CUBE_SIZE / 2,
                    -sizeOfMap / 2 * MapUtilities.CUBE_SIZE + MapUtilities.CUBE_SIZE * j)
                ;
            }
        }
    }

    static createTree(x, z, scene) {
        let treeTexture = new BABYLON.Texture(Tree, scene);
        let leafTexture = new BABYLON.Texture(Leaf, scene);
        //Creation of a material
        const treeMaterial = new BABYLON.StandardMaterial("treeTexture", scene);
        treeMaterial.diffuseTexture = treeTexture;
        const leafMaterial = new BABYLON.StandardMaterial("leafTexture", scene);
        leafMaterial.diffuseTexture = leafTexture;

        // SUR TOUS LES AXES Y -> On monte les meshes de la moitié de la hauteur du mesh en question.
        let tree = BABYLON.Mesh.CreateBox("tree", MapUtilities.CUBE_SIZE, scene);
        tree.position = new BABYLON.Vector3(x, MapUtilities.CUBE_SIZE / 2 + MapUtilities.CUBE_SIZE, z);
        tree.scaling = new BABYLON.Vector3(0.95, 0.95, 0.95);
        tree.material = treeMaterial;
        for (let i = 1; i < 4; i++) {
            let secondaryBox = tree.clone();
            secondaryBox.position.y = tree.position.y + MapUtilities.CUBE_SIZE * i;
        }

        let leaf = BABYLON.Mesh.CreateBox("leaf", MapUtilities.CUBE_SIZE, scene);
        leaf.position = new BABYLON.Vector3(x, MapUtilities.CUBE_SIZE / 2 + MapUtilities.CUBE_SIZE + MapUtilities.CUBE_SIZE * 4, z);
        leaf.scaling = new BABYLON.Vector3(0.95, 0.95, 0.95);
        leaf.material = leafMaterial;
        for (let i = -1; i <= 1; i++) {
            for (let j = -1; j <= 1; j++) {
                if (i === 0 && j === 0) continue;
                let secondaryBox = leaf.clone();
                secondaryBox.position.x = x + i;
                secondaryBox.position.z = z + j;
                secondaryBox.position.y = leaf.position.y - MapUtilities.CUBE_SIZE / 2;
            }
        }
    }

    static createLandPlot(x, y, z, scene) {
        //Creation of a material
        const grassMaterial = new BABYLON.StandardMaterial("groundTexture", scene);
        grassMaterial.diffuseTexture = new BABYLON.Texture(Grass, scene);

        let mainBox = BABYLON.Mesh.CreateBox("box1", MapUtilities.CUBE_SIZE, scene);
        mainBox.position = new BABYLON.Vector3(x, y, z);
        mainBox.scaling = new BABYLON.Vector3(0.95, 0.95, 0.95);
        mainBox.material = grassMaterial;
    }

    static createLight(scene) {
        // Création de notre lumière principale
        let light = new BABYLON.HemisphericLight("light1", new BABYLON.Vector3(0, 1, 0), scene);
        light.diffuse = new BABYLON.Color3(1, 1, 1);
        light.specular = new BABYLON.Color3(1, 1, 1);
        light.intensity = 1;
    }

    static createSkybox(scene) {
        // Skybox
        let skybox = BABYLON.MeshBuilder.CreateBox("skyBox", {size: 1000.0}, scene);
        let skyboxMaterial = new BABYLON.StandardMaterial("skyBox", scene);
        skyboxMaterial.backFaceCulling = false;
        skyboxMaterial.reflectionTexture = new BABYLON.CubeTexture("resources/textures/skybox", scene);
        skyboxMaterial.reflectionTexture.coordinatesMode = BABYLON.Texture.SKYBOX_MODE;
        skyboxMaterial.diffuseColor = new BABYLON.Color3(0, 0, 0);
        skyboxMaterial.specularColor = new BABYLON.Color3(0, 0, 0);
        skybox.material = skyboxMaterial;
        skybox.infiniteDistance = true;
    }

    static showWorldAxis(size, scene) {
        let makeTextPlane = (text, color, textSize) => {
            let dynamicTexture = new BABYLON.DynamicTexture("DynamicTexture", 50, scene, true);
            dynamicTexture.hasAlpha = true;
            dynamicTexture.drawText(text, 5, 40, "bold 36px Arial", color, "transparent", true);
            let plane = BABYLON.Mesh.CreatePlane("TextPlane", textSize, scene, true);
            plane.material = new BABYLON.StandardMaterial("TextPlaneMaterial", scene);
            plane.material.backFaceCulling = false;
            plane.material.specularColor = new BABYLON.Color3(0, 0, 0);
            plane.material.diffuseTexture = dynamicTexture;
            return plane;
        };
        let axisX = BABYLON.Mesh.CreateLines("axisX", [
            BABYLON.Vector3.Zero(), new BABYLON.Vector3(size, 0, 0), new BABYLON.Vector3(size * 0.95, 0.05 * size, 0),
            new BABYLON.Vector3(size, 0, 0), new BABYLON.Vector3(size * 0.95, -0.05 * size, 0)
        ], scene);
        axisX.color = new BABYLON.Color3(1, 0, 0);
        let xChar = makeTextPlane("X", "red", size / 10);
        xChar.position = new BABYLON.Vector3(0.9 * size, -0.05 * size, 0);
        let axisY = BABYLON.Mesh.CreateLines("axisY", [
            BABYLON.Vector3.Zero(), new BABYLON.Vector3(0, size, 0), new BABYLON.Vector3(-0.05 * size, size * 0.95, 0),
            new BABYLON.Vector3(0, size, 0), new BABYLON.Vector3(0.05 * size, size * 0.95, 0)
        ], scene);
        axisY.color = new BABYLON.Color3(0, 1, 0);
        let yChar = makeTextPlane("Y", "green", size / 10);
        yChar.position = new BABYLON.Vector3(0, 0.9 * size, -0.05 * size);
        let axisZ = BABYLON.Mesh.CreateLines("axisZ", [
            BABYLON.Vector3.Zero(), new BABYLON.Vector3(0, 0, size), new BABYLON.Vector3(0, -0.05 * size, size * 0.95),
            new BABYLON.Vector3(0, 0, size), new BABYLON.Vector3(0, 0.05 * size, size * 0.95)
        ], scene);
        axisZ.color = new BABYLON.Color3(0, 0, 1);
        let zChar = makeTextPlane("Z", "blue", size / 10);
        zChar.position = new BABYLON.Vector3(0, 0.05 * size, 0.9 * size);
    }

    static addMoveListener(scene) {
        scene.onKeyboardObservable.add(evt => {
            if (evt.type === BABYLON.KeyboardEventTypes.KEYDOWN) {
                switch (evt.event.key) {
                    case 'z':
                    case 's':
                    case 'q':
                    case 'd':
                        Communication.sendMessage("/command", JSON.stringify(evt.event.key));
                }
            }
        });
        scene._inputManager._onCanvasFocusObserver.callback(); //https://forum.babylonjs.com/t/camera-keyboard-issue/3335/5
    }

}