export default class Camera {

    constructor(scene, canvas) {
        this.canvas = canvas;
        this.scene = scene;
        this.arcRotateCamera = new BABYLON.ArcRotateCamera("ArcRotateCamera", -Math.PI/2, 7 * Math.PI/16, 12, new BABYLON.Vector3.Zero(), scene);
        this.arcRotateCamera.lowerBetaLimit = 0;
        this.arcRotateCamera.lowerRadiusLimit = 8;
        this.arcRotateCamera.upperBetaLimit = 9/10 * Math.PI/2;
        this.arcRotateCamera.upperRadiusLimit = 80;

        this.setArcRotateCamera();

        this.universalCamera = new BABYLON.UniversalCamera("UniversalCamera", new BABYLON.Vector3(0, 20, -50), scene);
        this.universalCamera.setTarget(new BABYLON.Vector3.Zero());
        // this.setUniversalCamera();
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


