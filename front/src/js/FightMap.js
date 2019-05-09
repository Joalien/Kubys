import Map from "./Map"
import Player from "./Player";

export default class FightMap {

    static PANEL;
    static PICKED_MESH = null;
    static HIGHLIGHT_LAYER;

    constructor() {
        this.advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("Menu de combat");
        FightMap.PANEL = new BABYLON.GUI.StackPanel();
        FightMap.PANEL.verticalAlignment = BABYLON.GUI.Control.VERTICAL_ALIGNMENT_TOP;
        this.advancedTexture.addControl(FightMap.PANEL);

    }

    static addButton = function (spell) {

        let transparentMeshes = [];
        let inScopeMeshes = [];
        //Let's connect to your account
        let connection = BABYLON.GUI.Button.CreateSimpleButton("button", spell.name);
        connection.height = "40px";
        connection.color = "white";
        connection.onPointerClickObservable.add(function () {
            FightMap.HIGHLIGHT_LAYER = new BABYLON.HighlightLayer("hl1", Map.SCENE);
            for (let mesh of Map.SCENE.meshes) {
                if (!FightMap.isMeshInsideScope(Player.CURRENT_PLAYER, mesh, spell)){
                    mesh.visibility = 0.5;
                    transparentMeshes.push(mesh);
                }else{
                    inScopeMeshes.push(mesh);
                }

            }



            window.addEventListener("pointermove", () =>FightMap.colorPickedMesh(inScopeMeshes));


            setTimeout(() => window.addEventListener("click", () => {
                for (let mesh of transparentMeshes) mesh.visibility = 1;
                FightMap.HIGHLIGHT_LAYER.dispose();
                window.removeEventListener("pointermove", () =>FightMap.colorPickedMesh(inScopeMeshes));
            }, {once: true}), 10);
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

    static colorPickedMesh = function (inScopeMeshes) {



        let newPickedMesh = Map.SCENE.pick(Map.SCENE.pointerX, Map.SCENE.pointerY).pickedMesh;

        if(newPickedMesh !== FightMap.PICKED_MESH && inScopeMeshes.includes(newPickedMesh)){
            if(FightMap.PICKED_MESH !== null){
                FightMap.HIGHLIGHT_LAYER.removeMesh(FightMap.PICKED_MESH);
            }
            FightMap.PICKED_MESH = newPickedMesh;
            FightMap.HIGHLIGHT_LAYER.addMesh(FightMap.PICKED_MESH, BABYLON.Color3.Green());

        }

    }

}
