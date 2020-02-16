import Player from "./Player";
import Game from "./Game";

import 'babylonjs-loaders'
import Camera from "./Camera";
import Gui from "./Gui";
import Communication from "./Communication";
import {AdvancedDynamicTexture, Button} from 'babylonjs-gui';
import firebase from "firebase";
import MapUtilities from "./MapUtilities";
import MainMap from "./MainMap";
import toastr from "toastr"
import './../../node_modules/toastr/build/toastr.css'; // FIXME find a better way to do this

export default class FightMap {

    static axe;
    static container;

    constructor(payload) {
        this.fightUid = payload;

        this.scene = new BABYLON.Scene(Game.ENGINE);
        this.camera = new Camera(this.scene, Game.CANVAS);
        Game.current_scene = Game.SCENES.push(this.scene) - 1;

        // Create Gui
        this.advancedTexture = AdvancedDynamicTexture.CreateFullscreenUI("Fight GUI", true, this.scene);

        let logOutButton = Button.CreateImageOnlyButton("Log out", "resources/images/icon_logout.png");
        Gui.setDefaultButtonCharacteristics(logOutButton, 48, -45);
        logOutButton.onPointerClickObservable.add(() => {
            firebase.auth().signOut();
        });
        this.advancedTexture.addControl(logOutButton);

        let winTheGameButton = Button.CreateImageOnlyButton("Fight !", "resources/images/icon_poison.png");
        Gui.setDefaultButtonCharacteristics(winTheGameButton, -45, 25);
        Gui.surroundWithColor(winTheGameButton);
        winTheGameButton.onPointerClickObservable.add(() => {
            Communication.sendMessage("/fight/" + this.fightUid + "/win", null);
        });
        this.advancedTexture.addControl(winTheGameButton);

        this.panel = new BABYLON.GUI.StackPanel();
        this.panel.verticalAlignment = BABYLON.GUI.Control.VERTICAL_ALIGNMENT_TOP;
        this.advancedTexture.addControl(this.panel);

        this.isCastingSpell = false;
        this.selectableMeshes = [];
        this.unselectableMeshes = [];

        //Uncomment to see axis (debug purpose)
        // MapUtilities.showWorldAxis(1, this.scene);
        MapUtilities.createLight(this.scene);
        MapUtilities.createSkybox(this.scene);

        MapUtilities.addMoveListener(this.scene);
        Communication.mockRestApi("/user/getAllMap", message => {
            MapUtilities.getAllMap(message, this.scene)
                .then(() => this.initializeActionManager());

        });
        Communication.sendMessage("/getAllMap", null);


        this.fightEventSubscription = Communication.clientSocket.subscribe("/broker/fight/" + this.fightUid, message => this.updateFightListener(message)); // TODO add your own logic here

        toastr.options = { // TODO put in a global conf file if multiple toastr
            "closeButton": true,
            "debug": false,
            "newestOnTop": true,
            "progressBar": false,
            "positionClass": "toast-bottom-right",
            "preventDuplicates": false,
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "3000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        toastr["info"]("Début du combat !");
        this.scene._inputManager._onCanvasFocusObserver.callback();

        BABYLON.SceneLoader.LoadAssetContainer("/resources/objects/axe/", "axe.obj", this.scene, container => {
            FightMap.axe = container.meshes[0];
            FightMap.axe.rotate(BABYLON.Axis.X, Math.PI / 2, BABYLON.Space.WORLD);
            FightMap.axe.scaling = new BABYLON.Vector3(0.03, 0.03, 0.03);
            FightMap.container = container;
        });

        Communication.mockRestApi("/user/getSpells", message => this.createComponentTreePanel(message));
        Communication.sendMessage("/getSpells", null);
    }

    initializeActionManager() {
        this.hightlightLayer = new BABYLON.HighlightLayer("highlight layer", this.scene);
        for (let mesh of this.scene.meshes) {
            mesh.actionManager = new BABYLON.ActionManager(this.scene);
            let castingSpellPredicate = new BABYLON.PredicateCondition(mesh.actionManager, () => this.isCastingSpell && this.selectableMeshes.includes(mesh));
            mesh.actionManager.registerAction(
                new BABYLON.ExecuteCodeAction(
                    BABYLON.ActionManager.OnPointerOverTrigger,
                    () => this.hightlightLayer.addMesh(mesh, BABYLON.Color3.Green()),
                    castingSpellPredicate
                )
            );
            mesh.actionManager.registerAction(
                new BABYLON.ExecuteCodeAction(
                    BABYLON.ActionManager.OnPointerOutTrigger,
                    () => this.hightlightLayer.removeMesh(mesh),
                    castingSpellPredicate
                )
            );
            mesh.actionManager.registerAction(
                new BABYLON.ExecuteCodeAction(
                    BABYLON.ActionManager.OnPickTrigger,
                    () => {
                        if (this.selectableMeshes.includes(mesh)) {
                            this.castSpell(this.scene.getMeshByID(Player.CURRENT_PLAYER_ID), mesh);
                            this.hightlightLayer.removeMesh(mesh);
                        }
                        this.toggleIsCastingSpell(false, this.selectableMeshes, this.unselectableMeshes);
                    },
                    castingSpellPredicate = new BABYLON.PredicateCondition(mesh.actionManager, () => this.isCastingSpell)
                )
            )
        }
    }

    toggleIsCastingSpell = (willBeCastingSpell, selectableMeshes, unselectableMeshes) => {
        this.isCastingSpell = willBeCastingSpell;
        if (this.isCastingSpell) {
            for (let mesh of unselectableMeshes) {
                mesh.visibility = 0.5;
            }
        } else {
            for (let mesh of unselectableMeshes) {
                mesh.visibility = 1;
            }
            selectableMeshes.length = 0;
            unselectableMeshes.length = 0;
        }
    };

    addButton = spell => {
        //Let's connect to your account
        let spellButton = BABYLON.GUI.Button.CreateSimpleButton("button", spell.name);
        spellButton.height = "40px";
        spellButton.width = "200px";
        spellButton.color = "white";
        spellButton.onPointerClickObservable.add(() => {
            this.partitioningSelectableMesh(spell);
            this.toggleIsCastingSpell(true, this.selectableMeshes, this.unselectableMeshes);
        });
        this.panel.addControl(spellButton);
    };

    partitioningSelectableMesh(spell) {
        for (let mesh of this.scene.meshes) {
            let isMeshSelectable = this.isInsideScope(this.scene.getMeshByID(Player.CURRENT_PLAYER_ID), mesh, spell)
                && this.hasLineOfSight(mesh, this.scene.getMeshByID(Player.CURRENT_PLAYER_ID));
            if (isMeshSelectable) {
                this.selectableMeshes.push(mesh);
            } else {
                this.unselectableMeshes.push(mesh);
            }
        }
        console.log("After partitioning : " + this.selectableMeshes.length);
    }

    createComponentTreePanel = message => {
        console.log("Spell received !");
        if (message.body) {
            //For each item in the map, we print it
            for (let spell of JSON.parse(message.body)) {
                this.addButton(spell);
            }
        } else {
            console.log("got empty message");
        }
    };

    isInsideScope = (player, mesh, spell) => {
        if (mesh.name === "skyBox") return false;
        let distance = Math.abs(player.position.x - mesh.position.x) + Math.abs(player.position.z - mesh.position.z);
        if (distance >= spell.minScope && distance <= spell.maxScope) {
            switch (spell.type/*.value*/) { // TODO uncomment
                case "CLASSIC":
                    distance = Math.abs(player.position.x - mesh.position.x) + Math.abs(player.position.z - mesh.position.z) + Math.abs(player.position.y - mesh.position.y);
                    return (distance <= spell.maxScope && distance >= spell.minScope);
                case "DROP":
                    return player.position.y > mesh.position.y;// Need to check if lanPlot above
                case "THROW":
                    return true;// Need to check if lanPlot above
                default:
                    console.error("No type found");
                    return false;
            }
        } else return false;
    };

    hasLineOfSight = (mesh, player) => {

        let origin = player.position;

        let direction = new BABYLON.Vector3(mesh.position.x - player.position.x, mesh.position.y - player.position.y, mesh.position.z - player.position.z);
        direction = direction.normalize();

        let ray = new BABYLON.Ray(origin, direction, 30);

        let hit = this.scene.pickWithRay(ray, (mesh) => mesh !== this.scene.getMeshByID(player.id));

        return (hit.pickedMesh === mesh || hit.pickedMesh === null); //Weird but else (-4, 0, -4) not found :O

    };

    castSpell = (player, mesh) => { // TODO take spell argument
        let time = 1; // second
        let direction = mesh.position.subtract(player.position);
        let direction2D = new BABYLON.Vector3(direction.x, direction.y, direction.z);
        direction2D.y = 0;
        direction2D.normalize();


        let ephemeralAxe = FightMap.axe.clone();
        ephemeralAxe.rotate(BABYLON.Axis.Y, Math.atan2(-direction2D.z, direction2D.x), BABYLON.Space.WORLD);
        ephemeralAxe.position = player.position;


        let translateAxeAnimation1 = new BABYLON.Animation("translateAxe", "position", 100 / time, BABYLON.Animation.ANIMATIONTYPE_VECTOR3, BABYLON.Animation.ANIMATIONLOOPMODE_CONSTANT);
        let rotateAxeAnimation = new BABYLON.Animation("rotateAxe", "rotation.y", 100 / time, BABYLON.Animation.ANIMATIONTYPE_FLOAT, BABYLON.Animation.ANIMATIONLOOPMODE_CYCLE);
        let keys1 = [];
        let keys2 = [];
        ephemeralAxe.animations = [];
        ephemeralAxe.animations.push(translateAxeAnimation1);
        ephemeralAxe.animations.push(rotateAxeAnimation);
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
        translateAxeAnimation1.setKeys(keys1);
        rotateAxeAnimation.setKeys(keys2);

        let animationGroup = new BABYLON.AnimationGroup("axeGroup");
        animationGroup.addTargetedAnimation(translateAxeAnimation1, ephemeralAxe);
        animationGroup.addTargetedAnimation(rotateAxeAnimation, ephemeralAxe);
        animationGroup.normalize(0, 100);
        animationGroup.play(true);

        this.scene.beginAnimation(ephemeralAxe, 0, 100, true, 1);

        setTimeout(() => this.scene.removeMesh(ephemeralAxe), time * 1000);

    };

    updateFightListener = message => {
        console.log("Received from /fight/" + this.fightUid + " : " + message.body);
        if (message.body === "true") {
            this.fightEventSubscription.unsubscribe();
            this.scene.dispose();
            this.advancedTexture.dispose();
            new MainMap();
        }
    };
}

