import Map from "./Map"

export default class FightMap {

    static PANEL;

    constructor() {
        this.advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("Menu de combat");
        FightMap.PANEL = new BABYLON.GUI.StackPanel();
        FightMap.PANEL.verticalAlignment = BABYLON.GUI.Control.VERTICAL_ALIGNMENT_TOP;
        this.advancedTexture.addControl(FightMap.PANEL);

    }

    static addButton = function (spell) {

        let transparentMeshes = [];
        //Let's connect to your account
        let connection = BABYLON.GUI.Button.CreateSimpleButton("button", spell.name);
        connection.height = "40px";
        connection.color = "white";
        connection.onPointerClickObservable.add(function () {
            for (let mesh of Map.SCENE.meshes) {
                if (Math.abs(mesh.position.x) >= 2 || Math.abs(mesh.position.z) >= 2) {
                    mesh.visibility = 0.5;
                    transparentMeshes.push(mesh);
                }

            }
            setTimeout(() => window.addEventListener("click", () => {
                for (let mesh of transparentMeshes) mesh.visibility = 1;
            }, {once: true}), 200);
        });


        FightMap.PANEL.addControl(connection);
    };


    static getSpells = function (message) {
        if (message.body) {
            new FightMap();

            //For each item in the map, we print it
            for (let spell of JSON.parse(message.body)) {
                FightMap.addButton(spell);
            }

        } else {
            console.log("got empty message");
        }
    };
}
