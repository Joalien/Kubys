export default class Camera {

    constructor(scene, canvas) {
        this.canvas = canvas;
        this.scene = scene;
        this.universalCamera = new BABYLON.UniversalCamera("UniversalCamera", new BABYLON.Vector3(0, 20, -50), scene);
        this.universalCamera.setTarget(new BABYLON.Vector3.Zero());
        this.setUniversalCamera();

        this.arcRotateCamera = new BABYLON.ArcRotateCamera("ArcRotateCamera", 10, 0.8, 10, new BABYLON.Vector3(0, 0, 0), scene);
        this.arcRotateCamera.setPosition(new BABYLON.Vector3(0, 30, -30));
    }


    setUniversalCamera() {
        this.scene.activeCamera = this.universalCamera;
        this.universalCamera.attachControl(this.canvas, true);
    }

    setArcRotateCamera() {
        // this.arcRotateCamera.setPosition(this.universalCamera.position);
        // this.arcRotateCamera.setTarget = this.universalCamera.direction;
        this.scene.activeCamera = this.arcRotateCamera;
        this.arcRotateCamera.attachControl(this.canvas, true);
    }
};


