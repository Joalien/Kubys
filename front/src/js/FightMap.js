import Map from "./Map"
import Player from "./Player";

import 'babylonjs-loaders'


export default class FightMap {

    static PANEL;
    static PICKED_MESH = null;
    static HIGHLIGHT_LAYER;
    static axe;
    static container;

    constructor() {
        console.log("new FightMap");

        this.advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("Menu de combat");
        FightMap.PANEL = new BABYLON.GUI.StackPanel();
        FightMap.PANEL.verticalAlignment = BABYLON.GUI.Control.VERTICAL_ALIGNMENT_TOP;

        this.advancedTexture.addControl(FightMap.PANEL);

        BABYLON.SceneLoader.LoadAssetContainer("/resources/", "axe.obj", Map.SCENE, function (container) {
            FightMap.axe = container.meshes[0];
            FightMap.axe.rotate(BABYLON.Axis.X, Math.PI / 2, BABYLON.Space.WORLD);
            FightMap.axe.scaling = new BABYLON.Vector3(0.03, 0.03, 0.03);
            FightMap.container = container;
        });

    }

    static addButton = function (spell) {

        let transparentMeshes = [];
        let inScopeMeshes = [];
        //Let's connect to your account
        let connection = BABYLON.GUI.Button.CreateSimpleButton("button", spell.name);
        connection.height = "40px";
        connection.color = "white";
        connection.onPointerClickObservable.add(function () {
            if(FightMap.HIGHLIGHT_LAYER) FightMap.HIGHLIGHT_LAYER.dispose();
            FightMap.HIGHLIGHT_LAYER = new BABYLON.HighlightLayer("hl1", Map.SCENE);
            for (let mesh of Map.SCENE.meshes) {
                if (!FightMap.isMeshInsideScope(Player.PLAYERS[Player.CURRENT_PLAYER_ID], mesh, spell) // If outside of the scope
                    || !FightMap.isLightOfSight(mesh, Player.PLAYERS[Player.CURRENT_PLAYER_ID])){// Or no light of sight
                    mesh.visibility = 0.5;
                    transparentMeshes.push(mesh);
                }else{

                    inScopeMeshes.push(mesh);
                }
            }

            window.addEventListener("pointermove", () => FightMap.highlightPickedMesh(inScopeMeshes));

            setTimeout(() => window.addEventListener("click", () => {// Remove spell casting
                for (let mesh of transparentMeshes) mesh.visibility = 1;
                FightMap.HIGHLIGHT_LAYER.dispose();
                window.removeEventListener("pointermove", () => FightMap.highlightPickedMesh(inScopeMeshes));

                if (inScopeMeshes.includes(Map.SCENE.pick(Map.SCENE.pointerX, Map.SCENE.pointerY).pickedMesh)){
                    FightMap.castSpell(Player.PLAYERS[Player.CURRENT_PLAYER_ID], Map.SCENE.pick(Map.SCENE.pointerX, Map.SCENE.pointerY).pickedMesh);
                }

            }, {once: true}), 10);

        });


        FightMap.PANEL.addControl(connection);
    };

    static getSpells = function (message) {
        if (message.body) {

            //For each item in the map, we print it
            for (let spell of JSON.parse(message.body)) {
                FightMap.addButton(spell);
            }

        } else {
            console.log("got empty message");
        }
    };


    static isMeshInsideScope = function(player, mesh, spell){
        if(mesh.name === "skyBox") return false;
        let distance = Math.abs(player.position.x - mesh.position.x) + Math.abs(player.position.z - mesh.position.z);
        if(distance <= spell.maxScope && distance >= spell.minScope){
            switch (spell.type) {
                case "CLASSIC":
                    distance = Math.abs(player.position.x - mesh.position.x) + Math.abs(player.position.z - mesh.position.z) + Math.abs(player.position.y - mesh.position.y);
                    return (distance <= spell.maxScope && distance >= spell.minScope);
                case "DROP":
                    return player.position.y > mesh.position.y;// Need to check if lanPlot above
                case "THROW":
                    return true;// Need to check if lanPlot above
                default:
                    console.log("No type found");
                    return false;

            }
        }else return false;
    };

    static highlightPickedMesh = function (inScopeMeshes) {

        let newPickedMesh = Map.SCENE.pick(Map.SCENE.pointerX, Map.SCENE.pointerY).pickedMesh;

        if(newPickedMesh !== FightMap.PICKED_MESH && inScopeMeshes.includes(newPickedMesh)){
            if(FightMap.PICKED_MESH !== null){
                FightMap.HIGHLIGHT_LAYER.removeMesh(FightMap.PICKED_MESH);
            }
            FightMap.PICKED_MESH = newPickedMesh;
            FightMap.HIGHLIGHT_LAYER.addMesh(FightMap.PICKED_MESH, BABYLON.Color3.Green());
        }

    };

    static isLightOfSight = function (mesh, player) {

        let origin = player.position;

        let direction = new BABYLON.Vector3(mesh.position.x-player.position.x,mesh.position.y-player.position.y,mesh.position.z-player.position.z);
        direction = direction.normalize();

        let ray = new BABYLON.Ray(origin, direction, 30);

        let hit = Map.SCENE.pickWithRay(ray, (mesh) => mesh !== Map.SCENE.getMeshByID(player.id));

        return (hit.pickedMesh === mesh || hit.pickedMesh === null); //Weird but else (-4, 0, -4) not found :O

    };

    static castSpell = function(player, mesh){
        let time = 1;//second
        let direction = mesh.position.subtract(player.position);
        let direction2D = new BABYLON.Vector3(direction.x, direction.y, direction.z);
        direction2D.y = 0;
        direction2D.normalize();


        let ephemeralAxe = FightMap.axe.clone();
        ephemeralAxe.rotate(BABYLON.Axis.Y, Math.atan2(-direction2D.z, direction2D.x), BABYLON.Space.WORLD);
        ephemeralAxe.position = player.position;


        let axeAnimation1 = new BABYLON.Animation("translateAxe", "position", 100/time, BABYLON.Animation.ANIMATIONTYPE_VECTOR3, BABYLON.Animation.ANIMATIONLOOPMODE_CONSTANT);
        let axeAnimation2 = new BABYLON.Animation("translateAxe", "rotation.y", 100/time, BABYLON.Animation.ANIMATIONTYPE_FLOAT, BABYLON.Animation.ANIMATIONLOOPMODE_CONSTANT);
        let keys1 = [];
        let keys2 = [];
        ephemeralAxe.animations = [];
        ephemeralAxe.animations.push(axeAnimation1);
        ephemeralAxe.animations.push(axeAnimation2);

        keys1.push({
            frame: 0,
            value: player.position
        });
        keys1.push({
            frame: 100,
            value: mesh.position
        });

        keys2.push({
            frame: 0,
            value: 0
        });
        keys2.push({
            frame: 100,
            value: -Math.PI/10
        });

        axeAnimation1.setKeys(keys1);
        axeAnimation2.setKeys(keys2);

        FightMap.container.addAllToScene();

        Map.SCENE.beginAnimation(ephemeralAxe, 0, 100, true, 1, );

        setTimeout(()=>Map.SCENE.removeMesh(ephemeralAxe), time*1000);

    }

}
