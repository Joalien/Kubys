import "babylonjs-loaders";
import Communication from "./Communication";
import Game from "./Game";
import Camera from "./Camera";
import MapUtilities from "./MapUtilities";
import {YAGL} from "./YAGL/yagl";
import Gui from "./Gui";
import MainMap from "./MainMap";
import {AdvancedDynamicTexture, Button} from 'babylonjs-gui';

export default class SkillTree {


    constructor() {
        // Appel des letiables nécéssaires

        this.scene = new BABYLON.Scene(Game.ENGINE);
        this.camera = new Camera(this.scene, Game.CANVAS);
        Game.current_scene = Game.SCENES.push(this.scene) - 1;

        // Create Gui
        this.advancedTexture = AdvancedDynamicTexture.CreateFullscreenUI("Menu Principal", true, this.scene);

        let skillTreeButton = Button.CreateImageOnlyButton("Arbre de compétence", "resources/images/icon_parchemin.png");
        Gui.setDefaultButtonCharacteristics(skillTreeButton, -45, 40);
        skillTreeButton.color = "orange";
        skillTreeButton.thickness = 4;
        Gui.surroundWithColor(skillTreeButton);
        skillTreeButton.onPointerClickObservable.add(() => {
            this.scene.dispose();
            new MainMap();
        });
        this.advancedTexture.addControl(skillTreeButton);

        Communication.mockRestApi("/user/getSpells", message => this.buildTree(message));
        Communication.sendMessage("/getSpells", null);


    }

    buildTree(message) {
        let builder = new YAGL.GraphBuilder(this.scene);
        // TODO generate me from message
        builder.buildUsingJSONObj({
                "graphicsFramework": "Babylonjs",
                "directed": true,
                "vertices": [
                    {"id": 1, "position": [0, -4, 0]},
                    {"id": 2, "position": [-1, -3, 0]},
                    {"id": 3, "position": [1, -3, 0]},
                    {"id": 4, "position": [-2, -2, 0]},
                    {"id": 5, "position": [0, -2, 0]},
                    {"id": 6, "position": [2, -2, 0]},
                    {"id": 7, "position": [-1, -1, 0]},
                    {"id": 8, "position": [1, -1, 0]},
                    {"id": 9, "position": [-2, 0, 0]},
                    {"id": 10, "position": [0, 0, 0]},
                    {"id": 11, "position": [2, 0, 0]},
                    {"id": 12, "position": [-1, 1, 0]},
                    {"id": 13, "position": [1, 1, 0]},
                    {"id": 14, "position": [-2, 2, 0]},
                    {"id": 15, "position": [0, 2, 0]},
                    {"id": 16, "position": [2, 2, 0]},
                    {"id": 17, "position": [0, 4, 0]},
                ],
                "edges": [
                    {"id": 1, "v1": 1, "v2": 2},
                    {"id": 2, "v1": 1, "v2": 3},
                    {"id": 3, "v1": 2, "v2": 4},
                    {"id": 4, "v1": 2, "v2": 5},
                    {"id": 7, "v1": 2, "v2": 7},
                    {"id": 5, "v1": 3, "v2": 5},
                    {"id": 6, "v1": 3, "v2": 6},
                    {"id": 8, "v1": 3, "v2": 8},
                    {"id": 9, "v1": 7, "v2": 9},
                    {"id": 10, "v1": 7, "v2": 10},
                    {"id": 13, "v1": 7, "v2": 12},
                    {"id": 11, "v1": 8, "v2": 10},
                    {"id": 12, "v1": 8, "v2": 11},
                    {"id": 14, "v1": 8, "v2": 13},
                    {"id": 15, "v1": 12, "v2": 14},
                    {"id": 16, "v1": 12, "v2": 15},
                    {"id": 17, "v1": 13, "v2": 15},
                    {"id": 18, "v1": 13, "v2": 16},
                    {"id": 19, "v1": 15, "v2": 17},
                ]
            }
        );

        //Uncomment to see axis (debug purpose)
        // MapUtilities.showWorldAxis(1, this.scene);
        MapUtilities.createLight(this.scene);
        MapUtilities.createSkybox(this.scene);
    }
}
