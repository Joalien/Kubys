let Gui = class Gui {

    constructor(camera) {
        this.advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("Menu Principal");
        this.panel = new BABYLON.GUI.StackPanel();

        this.advancedTexture.addControl(this.panel);
        let button = BABYLON.GUI.Button.CreateSimpleButton("buton", "Switch camera");

        button.width = 0.2;
        button.height = "40px";
        button.color = "white";
        button.onPointerClickObservable.add(function() {
            if(camera.scene.activeCamera instanceof BABYLON.UniversalCamera) camera.setArcRotateCamera();
            else if(camera.scene.activeCamera instanceof BABYLON.ArcRotateCamera) camera.setUniversalCamera();
        });

        this.panel.addControl(button);
    }

};
