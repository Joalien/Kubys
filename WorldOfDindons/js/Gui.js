let Gui = class Gui {

    constructor(camera) {
        let self = this;
        this.advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("Menu Principal");
        this.panel = new BABYLON.GUI.StackPanel();
        this.panel.verticalAlignment = BABYLON.GUI.Control.VERTICAL_ALIGNMENT_BOTTOM;
        this.advancedTexture.addControl(this.panel);

        //Button to switch between cameras
        let switchCamera = BABYLON.GUI.Button.CreateSimpleButton("buton", "Switch camera");
        switchCamera.width = 0.2;
        switchCamera.height = "40px";
        switchCamera.color = "white";
        switchCamera.onPointerClickObservable.add(function() {
            if(camera.scene.activeCamera instanceof BABYLON.UniversalCamera) camera.setArcRotateCamera();
            else if(camera.scene.activeCamera instanceof BABYLON.ArcRotateCamera) camera.setUniversalCamera();
        });
        this.panel.addControl(switchCamera);

        //Chose your name
        let input = new BABYLON.GUI.InputText();
        input.width = 0.2;
        input.height = "40px";
        input.text = "Username";
        input.color = "white";
        this.panel.addControl(input);


        //Let's connect to your account
        let connection = BABYLON.GUI.Button.CreateSimpleButton("buton", "Play");
        connection.width = 0.2;
        connection.height = "40px";
        connection.color = "white";
        connection.onPointerClickObservable.add(function() {
            let player = new Player();
            player.setUsername(input.text);
            self.panel.removeControl(input);
            self.panel.removeControl(connection);
        });
        this.panel.addControl(connection);
        //Select your name

        //Manuel
        let text1 = new BABYLON.GUI.TextBlock();
        text1.text = "Pour vous déplacer appuyez sur les touches z/q/s/d";
        text1.color = "white";
        text1.height = "40px";
        text1.fontSize = 24;
        this.panel.addControl(text1);

    }


};
