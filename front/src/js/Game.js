import Camera from './Camera.js';
import Gui from './Gui.js';
import Map from './Map.js';
import '../css/MainStyle.css';

import * as BABYLON from 'babylonjs';


// Page entièrement chargé, on lance le jeu
document.addEventListener("DOMContentLoaded", function () {
    new Game('renderCanvas');
}, false);

let Game = function(canvasId) {
    // Canvas et engine défini ici
    let canvas = document.getElementById(canvasId);
    let engine = new BABYLON.Engine(canvas, true);
    let _this = this;

    // On initie la scène avec une fonction associé à l'objet Game
    this.scene = this._initScene(engine);
    this.camera = new Camera(this.scene, canvas);
    this.gui = new Gui(this.scene, this.camera);



    let _map = new Map(_this);
    engine.runRenderLoop(function () {
        _this.scene.render();
    });

    // Ajuste la vue 3D si la fenetre est agrandi ou diminué
    window.addEventListener("resize", function () {
        if (engine) {
            engine.resize();
        }
    },false);

};

Game.prototype = {

    // Prototype d'initialisation de la scène
    _initScene : function(engine) {
        let scene = new BABYLON.Scene(engine);
        scene.clearColor = new BABYLON.Color4(0,0,0, 0);
        scene.gravity = new BABYLON.Vector3(0, -9.81, 0);
        scene.collisionsEnabled = true;
        let options = {
            groundColor: BABYLON.Color3.White()
        };
        // scene.createDefaultEnvironment(options);

        return scene;
    }
};
