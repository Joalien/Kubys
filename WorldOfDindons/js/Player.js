let Player = class Player {

    constructor(scene)
    {
        // Appel des variables nécéssaires
        const cubeSize = 3;
        const playerSize = 3;

        // SUR TOUS LES AXES Y -> On monte les meshes de la moitié de la hauteur du mesh en question.
        let player = BABYLON.Mesh.CreateBox("box1", cubeSize, scene);
        player.position = new BABYLON.Vector3(0, cubeSize / 2 + playerSize, 0);
        player.scaling = new BABYLON.Vector3(0.95, 0.95, 0.95);
        player.color = new BABYLON.Color3(1, 0, 0);

        let advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("UI");
        let rect1 = new BABYLON.GUI.Rectangle();
        rect1.width = 0.1;
        rect1.height = "40px";
        rect1.cornerRadius = 20;
        rect1.color = "Orange";
        rect1.thickness = 4;
        advancedTexture.addControl(rect1);

        this.username = new BABYLON.GUI.TextBlock();
        this.username.text = "Joalien";
        rect1.addControl(this.username);

        rect1.linkWithMesh(player);
        rect1.linkOffsetY = -30;

        window.addEventListener("keypress", function (evt) {
            switch (evt.key) {
                case 'z':
                    player.position.z = player.position.z + cubeSize;
                    break;
                case 's':
                    player.position.z = player.position.z - cubeSize;

                    break;
                case 'q':

                    player.position.x = player.position.x - cubeSize;
                    break;
                case 'd':
                    player.position.x = player.position.x + cubeSize;

                    break;
            }
        }, false);
    }

    setUsername (username){
        this.username.text = username;
    }

};