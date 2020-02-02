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

console.trace(process.env.NODE_ENV + ' mode');

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
document.addEventListener("DOMContentLoaded", () => {
    let game = new Game();
    game.init('renderCanvas');
}, false);


export default class Game {
    static CANVAS;
    static ENGINE;
    static CURRENT_SCENE;
    static MAIN_SCENE;
    static FIGHT_SCENE;

    init(canvasId) {
        // Canvas et engine défini ici
        Game.CANVAS = document.getElementById(canvasId);
        Game.ENGINE = new Engine(Game.CANVAS, true);

        // On initie les scènes avec une fonction associé à l'objet Game
        Game.MAIN_SCENE = new Scene(Game.ENGINE);
        Game.MAIN_SCENE.GUI = new Gui(Game.MAIN_SCENE);
        Game.MAIN_SCENE.CAMERA = new Camera(Game.MAIN_SCENE, Game.CANVAS);
        Game.MAIN_SCENE.MAP = new Map(Game.MAIN_SCENE, Game.MAIN_SCENE.CAMERA);
        Game.MAIN_SCENE.MAP.NAME = "Main map";
        Game.MAIN_SCENE.NAME = "Main scene";

        Game.FIGHT_SCENE = new Scene(Game.ENGINE);
        Game.FIGHT_SCENE.GUI = new FightMap();
        Game.FIGHT_SCENE.CAMERA = new Camera(Game.FIGHT_SCENE, Game.CANVAS);
        Game.FIGHT_SCENE.MAP = new Map(Game.FIGHT_SCENE, Game.FIGHT_SCENE.CAMERA);
        Game.FIGHT_SCENE.MAP.NAME = "Fight map";
        Game.FIGHT_SCENE.NAME = "Fight scene";

        Game.CURRENT_SCENE = Game.MAIN_SCENE;
        Game.ENGINE.runRenderLoop( () => {
            Game.CURRENT_SCENE.render();
        });

        new Communication();

            // Ajuste la vue 3D si la fenetre est agrandi ou diminué
        window.addEventListener("resize", () => {
            if (Game.ENGINE) {
                Game.ENGINE.resize();
            }
        },false);
    }

    static switchScene = (scene) => {
        // Remove old advancedTexture
        Game.CURRENT_SCENE.GUI.advancedTexture.dispose(); // Maybe bug if we want to switch back previous scene again
        // Game.PLAYER.advancedTexture.dispose();
        Game.CURRENT_SCENE = scene;
        Game.ENGINE.runRenderLoop( () => {
            Game.CURRENT_SCENE.render();
        });
        console.log("end switch scene");
    }
}

