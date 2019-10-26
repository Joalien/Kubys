import Camera from './Camera.js';
import Gui from './Gui.js';
import Map from './Map.js';
import "../css/MainStyle.css";
import "../../kubys_favicon.ico";

import {Engine, Scene} from 'babylonjs';
import FightMap from "./FightMap";
import Player from "./Player";
import Communication from "./Communication";

import firebase from "firebase/app";
import 'firebase/auth';

if (process.env.NODE_ENV !== 'production') {
    console.log('Development mode');
}


const firebaseConfig = {
    apiKey: "AIzaSyDmXTP9dZiqvzc2o1d0VREobnx4sFVduxY",
    authDomain: "kubys-id.firebaseapp.com",
    databaseURL: "https://kubys-id.firebaseio.com",
    projectId: "kubys-id",
    storageBucket: "kubys-id.appspot.com",
    messagingSenderId: "no-reply@kubys.fr",
    appID: "kubys-id",
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);


// Page entièrement chargé, on lance le jeu
document.addEventListener("DOMContentLoaded", function () {
    new Game('renderCanvas');
}, false);

let Game = function(canvasId) {

    // Canvas et engine défini ici
    let canvas = document.getElementById(canvasId);
    let engine = new Engine(canvas, true);
    let _this = this;

    // On initie la scène avec une fonction associé à l'objet Game
    this.scene = this._initScene(engine);
    this.camera = new Camera(this.scene, canvas);
    this.gui = new Gui();

    let _map = new Map(_this, this.camera);
    engine.runRenderLoop(function () {
        _this.scene.render();
    });

    new FightMap();
    Player.init(() => new Communication());

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
        return new Scene(engine);
    }
};
