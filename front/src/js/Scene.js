import 'firebase/auth';

export default class Scene {
    static GUI;
    static MAP;
    static CAMERA;

    constructor(engine) {
        Scene._initScene(engine)
    }

    // Prototype d'initialisation de la scène
    static _initScene = (engine) => {
        return new Scene(engine);
    };
};
