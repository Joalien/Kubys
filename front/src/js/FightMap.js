import Player from "./Player";
import Game from "./Game";
import Communication from "./Communication";

import 'babylonjs-loaders'

export default class FightMap { // TODO: renamed to FightGui

    static PANEL;
    static PICKED_MESH = null;
    static HIGHLIGHT_LAYER;
    static axe;
    static container;
    static advancedTexture;

    constructor() {
        FightMap.advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("Menu de combat", true, Game.FIGHT_SCENE);
        FightMap.PANEL = new BABYLON.GUI.StackPanel();
        FightMap.PANEL.verticalAlignment = BABYLON.GUI.Control.VERTICAL_ALIGNMENT_TOP;

        FightMap.advancedTexture.addControl(FightMap.PANEL);

        BABYLON.SceneLoader.LoadAssetContainer("/resources/objects/axe/", "axe.obj", Game.CURRENT_SCENE, function (container) {
            FightMap.axe = container.meshes[0];
            FightMap.axe.rotate(BABYLON.Axis.X, Math.PI / 2, BABYLON.Space.WORLD);
            FightMap.axe.scaling = new BABYLON.Vector3(0.03, 0.03, 0.03);
            FightMap.container = container;
        });
    }

    addButton = spell => {
        let transparentMeshes = [];
        let inScopeMeshes = [];
        //Let's connect to your account
        let connection = BABYLON.GUI.Button.CreateSimpleButton("button", spell.name);
        connection.height = "40px";
        connection.color = "white";
        connection.onPointerClickObservable.add(() => {
            if(FightMap.HIGHLIGHT_LAYER) FightMap.HIGHLIGHT_LAYER.dispose();
            FightMap.HIGHLIGHT_LAYER = new BABYLON.HighlightLayer("hl1", Game.CURRENT_SCENE);
            for (let mesh of Game.CURRENT_SCENE.meshes) {
                if (!FightMap.isMeshInsideScope(Player.PLAYERS[Player.CURRENT_PLAYER_ID], mesh, spell) // If outside of the scope
                    || !FightMap.isLightOfSight(mesh, Player.PLAYERS[Player.CURRENT_PLAYER_ID])) {// Or no light of sight
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

                if (inScopeMeshes.includes(Game.CURRENT_SCENE.pick(Game.CURRENT_SCENE.pointerX, Game.CURRENT_SCENE.pointerY).pickedMesh)) {
                    FightMap.castSpell(Player.PLAYERS[Player.CURRENT_PLAYER_ID], Game.CURRENT_SCENE.pick(Game.CURRENT_SCENE.pointerX, Game.CURRENT_SCENE.pointerY).pickedMesh);
                }

            }, {once: true}), 10);

        });


        FightMap.PANEL.addControl(connection);
    };

    getSpells = message => {
        if (message.body) {

            //For each item in the map, we print it
            for (let spell of JSON.parse(message.body)) {
                FightMap.addButton(spell);
            }
        } else {
            console.log("got empty message");
        }
    };

    static isMeshInsideScope = function(player, mesh, spell) {
        if(mesh.name === "skyBox") return false;
        let distance = Math.abs(player.position.x - mesh.position.x) + Math.abs(player.position.z - mesh.position.z);
        if(distance <= spell.maxScope && distance >= spell.minScope) {
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

    highlightPickedMesh = inScopeMeshes => {

        let newPickedMesh = Game.CURRENT_SCENE.pick(Game.CURRENT_SCENE.pointerX, Game.CURRENT_SCENE.pointerY).pickedMesh;

        if(newPickedMesh !== FightMap.PICKED_MESH && inScopeMeshes.includes(newPickedMesh)) {
            if(FightMap.PICKED_MESH !== null) {
                FightMap.HIGHLIGHT_LAYER.removeMesh(FightMap.PICKED_MESH);
            }
            FightMap.PICKED_MESH = newPickedMesh;
            FightMap.HIGHLIGHT_LAYER.addMesh(FightMap.PICKED_MESH, BABYLON.Color3.Green());
        }
    };

    isLightOfSight = (mesh, player) => {

        let origin = player.position;

        let direction = new BABYLON.Vector3(mesh.position.x-player.position.x,mesh.position.y-player.position.y,mesh.position.z-player.position.z);
        direction = direction.normalize();

        let ray = new BABYLON.Ray(origin, direction, 30);

        let hit = Game.CURRENT_SCENE.pickWithRay(ray, (mesh) => mesh !== Game.CURRENT_SCENE.getMeshByID(player.id));

        return (hit.pickedMesh === mesh || hit.pickedMesh === null); //Weird but else (-4, 0, -4) not found :O

    };

    castSpell = (player, mesh) => {
        let time = 1; // second
        let direction = mesh.position.subtract(player.position);
        let direction2D = new BABYLON.Vector3(direction.x, direction.y, direction.z);
        direction2D.y = 0;
        direction2D.normalize();


        let ephemeralAxe = FightMap.axe.clone();
        ephemeralAxe.rotate(BABYLON.Axis.Y, Math.atan2(-direction2D.z, direction2D.x), BABYLON.Space.WORLD);
        ephemeralAxe.position = player.position;


        let axeAnimation1 = new BABYLON.Animation("translateAxe", "position", 100/time, BABYLON.Animation.ANIMATIONTYPE_VECTOR3, BABYLON.Animation.ANIMATIONLOOPMODE_CONSTANT);
        let axeAnimation2 = new BABYLON.Animation("rotateAxe", "rotation.y", 100/time, BABYLON.Animation.ANIMATIONTYPE_FLOAT, BABYLON.Animation.ANIMATIONLOOPMODE_CYCLE);
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
            value: Math.PI
        });
        axeAnimation1.setKeys(keys1);
        axeAnimation2.setKeys(keys2);

        let animationGroup = new BABYLON.AnimationGroup("axeGroup");
        animationGroup.addTargetedAnimation(axeAnimation1, ephemeralAxe);
        animationGroup.addTargetedAnimation(axeAnimation2, ephemeralAxe);
        animationGroup.normalize(0, 100);
        animationGroup.play(true);

        // Game.CURRENT_SCENE.beginAnimation(ephemeralAxe, 0, 100, true, 1);

        setTimeout(() => Game.CURRENT_SCENE.removeMesh(ephemeralAxe), time*1000);

    };
}
