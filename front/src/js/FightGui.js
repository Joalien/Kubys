// import Player from "./Player";
// import Game from "./Game";
//
// import 'babylonjs-loaders'
// import {Scene} from "babylonjs/scene";
// import Camera from "./Camera";
// import Gui from "./Gui";
// import Communication from "./Communication";
//
// export default class FightGui {
//
//     static axe;
//     static container;
//
//     constructor() {
//
//         //
//         // // First, let's create the fight map
//         // Game.FIGHT_SCENE = new Scene(Game.ENGINE);
//         // Game.FIGHT_SCENE.GUI = new FightGui();
//         // Game.FIGHT_SCENE.CAMERA = new Camera(Game.FIGHT_SCENE, Game.CANVAS);
//         // Game.FIGHT_SCENE.MAP = new MainMap(Game.FIGHT_SCENE, Game.FIGHT_SCENE.CAMERA);
//         // Game.FIGHT_SCENE.MAP.NAME = "Fight map";
//         // Game.FIGHT_SCENE.NAME = "Fight scene";
//         //
//         // Game.switchScene(Game.FIGHT_SCENE);
//         //
//         // // Then, let's update the topic's subscription
//         // let fightId = message.body;
//         // Gui.subscription.unsubscribe();
//         // Communication.unsubscribeAll();
//         //
//         // Communication.updateSubscription(Game.FIGHT_SCENE);
//         // Communication.sendMessage("/getSpells", null);
//         // Communication.mockRestApi("/user/getSpells", message => Game.FIGHT_SCENE.GUI.createComponentTreePanel(message));
//         // Communication.clientSocket.subscribe("/broker/fight/" + fightId, payload => console.log("Received from /fight/" + fightId + " : " + payload.body));
//         // Communication.sendMessage("/fight/" + fightId + "/move/53", null);
//         // Communication.sendMessage("/fight/" + fightId + "/use/52/on/48", null);
//         // Communication.sendMessage("/fight/" + fightId + "/endTurn", null);
//         // Communication.sendMessage("/fight/" + fightId + "/winGame", null);
//
//
//         this.advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("Menu de combat", true, Game.FIGHT_SCENE);
//         this.panel = new BABYLON.GUI.StackPanel();
//         this.panel.verticalAlignment = BABYLON.GUI.Control.VERTICAL_ALIGNMENT_TOP;
//         this.advancedTexture.addControl(this.panel);
//
//         BABYLON.SceneLoader.LoadAssetContainer("/resources/objects/axe/", "axe.obj", Game.FIGHT_SCENE, function (container) {
//             FightGui.axe = container.meshes[0];
//             FightGui.axe.rotate(BABYLON.Axis.X, Math.PI / 2, BABYLON.Space.WORLD);
//             FightGui.axe.scaling = new BABYLON.Vector3(0.03, 0.03, 0.03);
//             FightGui.container = container;
//         });
//     }
//
//     addButton = spell => {
//         let transparentMeshes = [];
//         let inScopeMeshes = [];
//         //Let's connect to your account
//         let connection = BABYLON.GUI.Button.CreateSimpleButton("button", spell.name);
//         connection.height = "40px";
//         connection.color = "white";
//         connection.onPointerClickObservable.add(() => {
//             if (this.hightlightLayer) this.hightlightLayer.dispose();
//             this.hightlightLayer = new BABYLON.HighlightLayer("hl1", Game.CURRENT_SCENE);
//             for (let mesh of Game.CURRENT_SCENE.meshes) {
//                 // if (mesh === FightGui.axe) continue;
//                 if (!FightGui.isMeshInsideScope(Game.CURRENT_SCENE.getMeshByID(Player.CURRENT_PLAYER_ID), mesh, spell) // If outside of the scope
//                     || !this.isLightOfSight(mesh, Game.CURRENT_SCENE.getMeshByID(Player.CURRENT_PLAYER_ID))) { // Or no light of sight
//                     mesh.visibility = 0.5;
//                     transparentMeshes.push(mesh);
//                 } else {
//                     inScopeMeshes.push(mesh);
//                 }
//             }
//
//             window.addEventListener("pointermove", () => this.highlightPickedMesh(inScopeMeshes));
//
//             setTimeout(() => window.addEventListener("click", () => { // Remove spell casting
//                 for (let mesh of transparentMeshes) mesh.visibility = 1;
//                 this.hightlightLayer.dispose();
//                 window.removeEventListener("pointermove", () => this.highlightPickedMesh(inScopeMeshes));
//
//                 if (inScopeMeshes.includes(Game.CURRENT_SCENE.pick(Game.CURRENT_SCENE.pointerX, Game.CURRENT_SCENE.pointerY).pickedMesh)) {
//                     this.castSpell(Game.CURRENT_SCENE.getMeshByID(Player.CURRENT_PLAYER_ID), Game.CURRENT_SCENE.pick(Game.CURRENT_SCENE.pointerX, Game.CURRENT_SCENE.pointerY).pickedMesh);
//                 }
//
//             }, {once: true}), 10);
//
//         });
//         this.panel.addControl(connection);
//     };
//
//     createComponentTreePanel = message => {
//         console.log("Spell received !");
//         if (message.body) {
//             //For each item in the map, we print it
//             let i = 0;
//             for (let spell of JSON.parse(message.body)) {
//                 this.addButton(spell);
//                 if (i++ > 3) break; // Todo: remove this line
//             }
//         } else {
//             console.log("got empty message");
//         }
//     };
//
//     static isMeshInsideScope = (player, mesh, spell) => {
//         if (mesh.name === "skyBox")  return false;
//         let distance = Math.abs(player.position.x - mesh.position.x) + Math.abs(player.position.z - mesh.position.z);
//         if (distance <= spell.maxScope && distance >= spell.minScope) {
//             switch (spell.type/*.value*/) { // TODO uncomment
//                 case "CLASSIC":
//                     distance = Math.abs(player.position.x - mesh.position.x) + Math.abs(player.position.z - mesh.position.z) + Math.abs(player.position.y - mesh.position.y);
//                     return (distance <= spell.maxScope && distance >= spell.minScope);
//                 case "DROP":
//                     return player.position.y > mesh.position.y;// Need to check if lanPlot above
//                 case "THROW":
//                     return true;// Need to check if lanPlot above
//                 default:
//                     console.log("No type found");
//                     return false;
//             }
//         } else return false;
//     };
//
//     highlightPickedMesh = inScopeMeshes => {
//
//         let newPickedMesh = Game.CURRENT_SCENE.pick(Game.CURRENT_SCENE.pointerX, Game.CURRENT_SCENE.pointerY).pickedMesh;
//
//         if (newPickedMesh !== this.pickedMesh && inScopeMeshes.includes(newPickedMesh)) {
//             if (this.pickedMesh) {
//                 this.hightlightLayer.removeMesh(this.pickedMesh);
//             }
//             this.pickedMesh = newPickedMesh;
//             this.hightlightLayer.addMesh(this.pickedMesh, BABYLON.Color3.Green());
//         }
//     };
//
//     isLightOfSight = (mesh, player) => {
//
//         let origin = player.position;
//
//         let direction = new BABYLON.Vector3(mesh.position.x-player.position.x,mesh.position.y-player.position.y,mesh.position.z-player.position.z);
//         direction = direction.normalize();
//
//         let ray = new BABYLON.Ray(origin, direction, 30);
//
//         let hit = Game.CURRENT_SCENE.pickWithRay(ray, (mesh) => mesh !== Game.CURRENT_SCENE.getMeshByID(player.id));
//
//         return (hit.pickedMesh === mesh || hit.pickedMesh === null); //Weird but else (-4, 0, -4) not found :O
//
//     };
//
//     castSpell = (player, mesh) => {
//         let time = 1; // second
//         let direction = mesh.position.subtract(player.position);
//         let direction2D = new BABYLON.Vector3(direction.x, direction.y, direction.z);
//         direction2D.y = 0;
//         direction2D.normalize();
//
//
//         let ephemeralAxe = FightGui.axe.clone();
//         ephemeralAxe.rotate(BABYLON.Axis.Y, Math.atan2(-direction2D.z, direction2D.x), BABYLON.Space.WORLD);
//         ephemeralAxe.position = player.position;
//
//
//         let translateAxeAnimation1 = new BABYLON.Animation("translateAxe", "position", 100/time, BABYLON.Animation.ANIMATIONTYPE_VECTOR3, BABYLON.Animation.ANIMATIONLOOPMODE_CONSTANT);
//         let rotateAxeAnimation = new BABYLON.Animation("rotateAxe", "rotation.y", 100/time, BABYLON.Animation.ANIMATIONTYPE_FLOAT, BABYLON.Animation.ANIMATIONLOOPMODE_CYCLE);
//         let keys1 = [];
//         let keys2 = [];
//         ephemeralAxe.animations = [];
//         ephemeralAxe.animations.push(translateAxeAnimation1);
//         ephemeralAxe.animations.push(rotateAxeAnimation);
//         keys1.push({
//             frame: 0,
//             value: player.position
//         });
//
//         keys1.push({
//             frame: 100,
//             value: mesh.position
//         });
//         keys2.push({
//             frame: 0,
//             value: 0
//         });
//
//         keys2.push({
//             frame: 100,
//             value: Math.PI
//         });
//         translateAxeAnimation1.setKeys(keys1);
//         rotateAxeAnimation.setKeys(keys2);
//
//         let animationGroup = new BABYLON.AnimationGroup("axeGroup");
//         animationGroup.addTargetedAnimation(translateAxeAnimation1, ephemeralAxe);
//         animationGroup.addTargetedAnimation(rotateAxeAnimation, ephemeralAxe);
//         animationGroup.normalize(0, 100);
//         animationGroup.play(true);
//
//         Game.CURRENT_SCENE.beginAnimation(ephemeralAxe, 0, 100, true, 1);
//
//         setTimeout(() => Game.CURRENT_SCENE.removeMesh(ephemeralAxe), time*1000);
//
//     };
// }
