import "../css/MainStyle.css";
import "../../kubys_favicon.ico";
import {Engine} from 'babylonjs';
import Communication from "./Communication";
import firebase from "firebase/app";
import 'firebase/auth';
import SelectionRing from "./SelectionRing";

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
    static SCENES = [];
    static current_scene; // integer

    init(canvasId) {
        // Canvas et engine défini ici
        Game.CANVAS = document.getElementById(canvasId);
        Game.ENGINE = new Engine(Game.CANVAS, true);

        new Communication(() => {
           new SelectionRing();
        });

        Game.ENGINE.runRenderLoop( () => {
            if (Game.current_scene !== undefined) {
                Game.SCENES[Game.current_scene].render();
            }
        });

        // On initie les scènes avec une fonction associé à l'objet Game
        // Game.MAIN_SCENE = new Scene(Game.ENGINE);
        // Game.MAIN_SCENE.GUI = new Gui(Game.MAIN_SCENE);
        // Game.MAIN_SCENE.CAMERA = new Camera(Game.MAIN_SCENE, Game.CANVAS);
        // Game.MAIN_SCENE.MAP = new MainMap(Game.MAIN_SCENE, Game.MAIN_SCENE.CAMERA);
        // Game.MAIN_SCENE.MAP.NAME = "Main map";
        // Game.MAIN_SCENE.NAME = "Main scene";

        // Ajuste la vue 3D si la fenetre est agrandi ou diminué
        window.addEventListener("resize", () => {
            if (Game.ENGINE) {
                Game.ENGINE.resize();
            }
        },false);
    }

}

